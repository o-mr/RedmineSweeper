package com.kz.redminesweeper.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

    private IssueListAdapter issueListAdapter;

    @ViewById
    SwipeRefreshLayout refresh;

    @ViewById
    ListView listView;

    private int offset;

    private int totalCount = -1;

    private boolean isLoading;

    private boolean isLoaded;

    private static final int LIMIT = 25;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        listView.setOnScrollListener(this);
        issueListAdapter = new IssueListAdapter(getActivity(), R.layout.list_item_issue, R.id.base_layout, new ArrayList<Issue>());
        listView.setAdapter(issueListAdapter);
        listView.setOnItemClickListener(this);
        refresh.setOnRefreshListener(this);
    }

    void downloadIssues() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (isLoading) return;
        if (isLoaded) return;
        if (app.getFilter() == null) return;
        refresh.setRefreshing(true);
        isLoading = true;
        downloadIssuesAsync();
    }

    @Background
    void downloadIssuesAsync() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Issues issues = null;
        IssuesFilter filter = app.getFilter();
        if (filter instanceof Status) {
            issues = app.getRedmine().getMyIssuesByProjectIdAndStatusId(project.getId(), filter.getId(), offset, LIMIT);
        } else if (filter instanceof Tracker) {
            issues = app.getRedmine().getMyIssuesByProjectIdAndTrackerId(project.getId(), filter.getId(), offset, LIMIT);
        } else if (filter instanceof Watcher) {
            issues = app.getRedmine().getIssuesByProjectIdAndWatcherId(project.getId(), filter.getId(), offset, LIMIT);
        }
        updateIssueList(issues);
    }

    @UiThread
    void updateIssueList(Issues issues) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (issueListAdapter.getCount() == 0) {
            totalCount = issues.getTotal_count();
        }
        offset += issues.getIssues().size();
        isLoaded = issues.getIssues().size() < LIMIT;
        for (Issue issue : issues) {
            issueListAdapter.remove(issue);
        }
        issueListAdapter.addAll(issues.getIssues());
        issueListAdapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
        isLoading = false;
    }

    public static IssueListFragment newInstance(Project project) {
        return IssueListFragment_.builder().project(project).build();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (totalItemCount > (firstVisibleItem + visibleItemCount) + ((int) LIMIT / 5)) return;
        downloadIssues();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Toast.makeText(getActivity(), issueListAdapter.getItem(position).getSubject(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        clearList();
        downloadIssues();
    }

    public void clearList() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (issueListAdapter == null) return;
        isLoaded = false;
        issueListAdapter.clear();
        offset = 0;
    }

    public int getTotalCount() {
        return totalCount;
    }

}
