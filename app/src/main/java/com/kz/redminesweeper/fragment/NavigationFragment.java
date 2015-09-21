package com.kz.redminesweeper.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.kz.redminesweeper.ActivityErrorReceiver;
import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.adapter.AccountListAdapter;
import com.kz.redminesweeper.adapter.FilterListAdapter;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.ListItem;
import com.kz.redminesweeper.bean.Projects;
import com.kz.redminesweeper.bean.Queries;
import com.kz.redminesweeper.bean.Query;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Tracker;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.bean.Watcher;
import com.kz.redminesweeper.rest.RedmineAccess;
import com.kz.redminesweeper.rest.RedmineRestService;
import com.kz.redminesweeper.view.AccountFooter;
import com.kz.redminesweeper.view.AccountFooter_;
import com.kz.redminesweeper.view.AccountHeader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_navigation)
public class NavigationFragment extends Fragment {

    @App
    RmSApplication app;

    @ViewById
    AccountHeader accountHeader;

    @ViewById
    ListView filterList;

    @ViewById
    ListView accountList;

    private AccountFooter accountFooter;

    private FilterListAdapter filterListAdapter;

    private AccountListAdapter accountListAdapter;

    private NavigationCallBacks navigationCallBacks;

    private ActivityErrorReceiver errorReceiver;

    private List<ListItem> defaultFilters;

    private List<Query> queryFilters;

    private List<Tracker> trackerFilters;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountHeader.bind(app.getAccountManager().getEnableAccount());
        createFilterList();
        createAccountList();
    }

    void createFilterList() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        defaultFilters = new ArrayList<>();
        defaultFilters.add(new Status("o", getString(R.string.filter_open)));
        defaultFilters.add(new Status("c", getString(R.string.filter_close)));
        defaultFilters.add(new Watcher("me", getString(R.string.filter_watch)));
        defaultFilters.add(new Status("*", getString(R.string.filter_all)));
        filterListAdapter = new FilterListAdapter(getActivity(), R.layout.list_item_filter, R.id.base_layout, defaultFilters);
        filterList.setAdapter(filterListAdapter);
        updateQueries();
        updateTrackers();
    }

    void updateQueries() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        app.getRedmine().downloadQueries(new RedmineAccess.RestResultListener<Queries>() {
            @Override
            public void onSuccessful(Queries result) {
                queryFilters = result.getQueries();
                updateFilterList();
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                breakDownload(msgId, e);
            }
        });
    }

    void updateTrackers() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        app.getRedmine().downloadTrackers(new RedmineAccess.RestResultListener<Trackers>() {
            @Override
            public void onSuccessful(Trackers result) {
                trackerFilters = result.getTrackers();
                updateFilterList();
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                breakDownload(msgId, e);
            }
        });
    }

    @UiThread
    void updateFilterList() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (queryFilters == null || trackerFilters == null) return;
        filterListAdapter.addPartition(getString(R.string.label_filter_queries));
        filterListAdapter.addAll(queryFilters);
        filterListAdapter.addPartition(getString(R.string.label_filter_trackers));
        filterListAdapter.addAll(trackerFilters);
        filterListAdapter.notifyDataSetChanged();
        queryFilters = null;
        trackerFilters = null;
        selectFilter(0);
    }

    @UiThread
    void breakDownload(int msgId, Throwable e) {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
        if (errorReceiver != null) errorReceiver.onReceivedError(msgId, e);
    }

    void createAccountList() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountFooter = AccountFooter_.build(getActivity());
        accountList.addFooterView(accountFooter);
        accountListAdapter = new AccountListAdapter(getActivity(), R.layout.list_item_account, R.id.base_layout, app.getAccountManager().getAccounts());
        accountList.setAdapter(accountListAdapter);
        accountListAdapter.notifyDataSetChanged();
        selectAccount(app.getAccountManager().indexOfEnableAccount());
    }

    @ItemClick(R.id.filter_list)
    void selectFilter(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        filterList.setItemChecked(position, true);
        filterList.setSelection(position);
        IssuesFilter filter = (IssuesFilter)filterListAdapter.getItem(position);
        if (navigationCallBacks == null) return;
        navigationCallBacks .onChangeFilter(filter);
    }

    @ItemClick(R.id.account_list)
    void selectAccount(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountList.setItemChecked(position, true);
        accountList.setSelection(position);
        final Account account = accountListAdapter.getItem(position);
        if (account.equals(app.getAccountManager().getEnableAccount())) return;
        app.getAccountManager().authenticate(account, new RedmineAccess.RestResultListener<Account>() {
            @Override
            public void onSuccessful(Account result) {
                switchAccount(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                startAuthentication(account);
            }
        });
    }

    @UiThread
    void switchAccount(Account account) {
        if (navigationCallBacks == null) return;
        navigationCallBacks.onSwitchAccount(account);
    }

    @UiThread
    void startAuthentication(Account account) {
        if (navigationCallBacks == null) return;
        navigationCallBacks.onStartAuthentication(account);
    }

    @Click(R.id.account_header)
    void switchNavigation() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (accountList.getVisibility() == View.VISIBLE) {
            accountList.setVisibility(View.GONE);
            filterList.setVisibility(View.VISIBLE);
        } else {
            accountList.setVisibility(View.VISIBLE);
            filterList.setVisibility(View.GONE);
        }
        accountHeader.changeNavigationMode();
    }

    public void setNavigationCallBacks(NavigationCallBacks navigationCallBacks) {
        this.navigationCallBacks = navigationCallBacks;
    }

    public void setErrorReceiver(ActivityErrorReceiver errorReceiver) {
        this.errorReceiver = errorReceiver;
    }

    public static NavigationFragment newInstance() {
        return NavigationFragment_.builder().build();
    }

    public interface NavigationCallBacks {
        void onChangeFilter(IssuesFilter filter);
        void onSwitchAccount(Account account);
        void onStartAuthentication(Account account);
    }

}
