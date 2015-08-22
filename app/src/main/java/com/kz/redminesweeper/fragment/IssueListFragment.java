package com.kz.redminesweeper.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.adapter.IssueListAdapter;
import com.kz.redminesweeper.bean.Issue;
import com.kz.redminesweeper.bean.Issues;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Tracker;
import com.kz.redminesweeper.bean.Watcher;
import com.kz.redminesweeper.rest.RedmineRestHelper;
import com.kz.redminesweeper.rest.RedmineRestService;
import com.kz.redminesweeper.view.BlankWall;
import com.kz.redminesweeper.view.BlankWall_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.fragment_issue_list)
public class IssueListFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    @App
    RmSApplication app;

    @FragmentArg
    Project project = new Project();

    @ViewById
    SwipeRefreshLayout refresh;

    @ViewById
    LinearLayout baseLayout;

    @ViewById
    ListView listView;

    BlankWall noTickets;

    private IssueListAdapter issueListAdapter;

    private IssuesFilter filter;

    private int offset;

    private int totalCount;

    private boolean isLoading;

    private boolean isLoaded;

    private static final int LIMIT = 25;

    @AfterViews
    public void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        noTickets = BlankWall_.build(getActivity());
        noTickets.setTitle(R.string.label_no_tickets, 24);
        noTickets.setWallColor(R.color.bg_transparency);
        listView.setOnScrollListener(this);
        issueListAdapter = new IssueListAdapter(getActivity(), R.layout.list_item_issue, R.id.base_layout, new ArrayList<Issue>());
        listView.setAdapter(issueListAdapter);
        listView.setOnItemClickListener(this);
        refresh.setOnRefreshListener(this);
    }

    void startDownloadIssues() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (isLoading) return;
        if (isLoaded) return;
        if (filter == null) return;
        refresh.setRefreshing(true);
        downloadIssues();
    }

    void downloadIssues() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        isLoading = true;
        app.getRedmine().executeRest(new RedmineRestHelper.RestExecutor<Issues>() {
            @Override
            public Issues execute(RedmineRestService redmine) {
                if (filter instanceof Status) {
                    return redmine.getMyIssuesByProjectIdAndStatusId(project.getId(), filter.getId(), offset, LIMIT);
                } else if (filter instanceof Tracker) {
                    return redmine.getMyIssuesByProjectIdAndTrackerId(project.getId(), filter.getId(), offset, LIMIT);
                } else if (filter instanceof Watcher) {
                    return redmine.getIssuesByProjectIdAndWatcherId(project.getId(), filter.getId(), offset, LIMIT);
                } else {
                    return null;
                }
            }

            @Override
            public void onSuccessful(Issues result) {
                updateIssueList(result);
            }

            @Override
            public void onFailed(RedmineRestHelper.RestError restError, int msgId, Throwable e) {
            }

        });
    }

    @UiThread
    void updateIssueList(Issues issues) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (offset == 0) {
            totalCount = issues.getTotal_count();
        }
        offset += issues.getIssues().size();
        isLoaded = issues.getIssues().size() < LIMIT;
        for (Issue issue : issues) {
            issueListAdapter.remove(issue);
        }
        issueListAdapter.addAll(issues.getIssues());
        issueListAdapter.notifyDataSetChanged();
        if (offset == 0) {
            noTickets.show(baseLayout);
        }
        refresh.setRefreshing(false);
        isLoading = false;
        if (getActivity() == null) return;
        ((IssueListCallbacks) getActivity()).onLoadedIssues(this, issues);
    }

    @Override
    public void onRefresh() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (issueListAdapter == null) return;
        noTickets.hide();
        listView.clearChoices();
        isLoaded = false;
        issueListAdapter.clear();
        offset = 0;
        startDownloadIssues();
    }

    public void onChangeFilter(IssuesFilter filter) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        this.filter = filter;
        onRefresh();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (totalItemCount > (firstVisibleItem + visibleItemCount) + ((int) LIMIT / 5)) return;
        startDownloadIssues();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Toast.makeText(getActivity(), issueListAdapter.getItem(position).getSubject(), Toast.LENGTH_SHORT).show();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public static IssueListFragment newInstance(Project project) {
        return IssueListFragment_.builder().project(project).build();
    }

    public interface IssueListCallbacks {
        void onLoadedIssues(Fragment fragment,  Issues issues);
    }

}
