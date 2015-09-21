package com.kz.redminesweeper.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kz.redminesweeper.AccountSettingsActivity_;
import com.kz.redminesweeper.ActivityErrorReceiver;
import com.kz.redminesweeper.IssueDetailActivity;
import com.kz.redminesweeper.IssueDetailActivity_;
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
import com.kz.redminesweeper.rest.InvalidQueryException;
import com.kz.redminesweeper.rest.RedmineAccess;
import com.kz.redminesweeper.rest.RedmineRestService;
import com.kz.redminesweeper.view.BlankWall;
import com.kz.redminesweeper.view.BlankWall_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.fragment_issue_list)
public class IssueListFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    @App
    RmSApplication app;

    @FragmentArg
    Project project = new Project();

    @ViewById
    SwipeRefreshLayout refresh;

    @ViewById
    LinearLayout baseLayout;

    @ViewById
    ListView issueList;

    private BlankWall noTickets;

    private IssueListAdapter issueListAdapter;

    private int offset;

    private int totalCount;

    private boolean isLoading;

    private boolean isLoaded;

    private static final int LIMIT = 25;

    private ActivityErrorReceiver errorReceiver;

    @FragmentArg
    IssuesFilter filter;

    @AfterViews
    public void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        noTickets = BlankWall_.build(getActivity());
        noTickets.setWallColor(R.color.bg_transparency);
        noTickets.setBlankWallCallBacks(new BlankWall.BlankWallCallBacks() {
            @Override
            public void onClick() {
                onRefresh();
            }
        });
        issueList.setOnScrollListener(this);
        issueListAdapter = new IssueListAdapter(getActivity(), R.layout.list_item_issue, R.id.base_layout, new ArrayList<Issue>());
        issueList.setAdapter(issueListAdapter);
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
        app.getRedmine().downloadIssues(project, offset, LIMIT, filter, new RedmineAccess.RestResultListener<Issues>() {
                @Override
                public void onSuccessful(Issues result) {
                    updateIssueList(result);
                }

                @Override
                public void onFailed(int msgId, Throwable e) {
                    breakDownloadIssues(msgId, e);
                }
            }
        );
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
            noTickets.setTitle(R.string.label_no_tickets, 24);
            noTickets.show(baseLayout);
        }
        refresh.setRefreshing(false);
        isLoading = false;
        if (getActivity() == null) return;
        ((IssueListCallbacks) getActivity()).onLoadedIssues(this, issues);
    }

    @UiThread
    void breakDownloadIssues(int msgId, Throwable e) {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
        refresh.setRefreshing(false);
        isLoading = false;
        if (e instanceof InvalidQueryException) {
            noTickets.setTitle(R.string.label_invalid_query, 24);
            noTickets.show(baseLayout);
        } else {
            if (errorReceiver != null) errorReceiver.onReceivedError(msgId, e);
        }
    }

    @Override
    public void onRefresh() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (issueListAdapter == null) return;
        noTickets.hide();
        issueList.clearChoices();
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

    @ItemClick(R.id.issue_list)
    void selectIssue(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Issue issue = issueListAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), IssueDetailActivity_.class);
        intent.putExtra("issue", issue);
        startActivityForResult(intent, IssueDetailActivity.REQUEST_CODE);
    }

    public void setErrorReceiver(ActivityErrorReceiver errorReceiver) {
        this.errorReceiver = errorReceiver;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public static IssueListFragment newInstance(Project project, IssuesFilter filter) {
        return IssueListFragment_.builder().project(project).filter(filter).build();
    }

    public interface IssueListCallbacks {
        void onLoadedIssues(Fragment fragment,  Issues issues);
    }

}
