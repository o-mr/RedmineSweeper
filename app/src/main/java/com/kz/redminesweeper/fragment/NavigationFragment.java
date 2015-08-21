package com.kz.redminesweeper.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.adapter.AccountListAdapter;
import com.kz.redminesweeper.adapter.FilterListAdapter;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.bean.Watcher;
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
public class NavigationFragment extends Fragment implements AccountManager.AccountAuthenticator {

    @App
    RmSApplication app;

    @ViewById
    AccountHeader accountHeader;

    @ViewById
    ListView filterList;

    @ViewById
    ListView accountList;

    AccountFooter accountFooter;

    private FilterListAdapter filterListAdapter;

    private AccountListAdapter accountListAdapter;

    @Override
    public void onAttach(Activity activity) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onAttach(activity);
    }

    @AfterViews
    public void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        createFilterList();
        createAccountList();
    }

    @Override
    public void onStart() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onStart();
        accountHeader.bind(app.getAccountManager().getEnableAccount());
    }

    void createFilterList() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        List<IssuesFilter> filters = new ArrayList<>();
        filters.add(new Status("o", getString(R.string.filter_open)));
        filters.add(new Status("c", getString(R.string.filter_close)));
        filters.add(new Watcher("me", getString(R.string.filter_watch)));
        filters.add(new Status("*", getString(R.string.filter_all)));
        downloadFilter(filters);
    }

    @Background
    void downloadFilter(List<IssuesFilter> filters) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Trackers trackers = app.getRedmine().getTrackers();
        filters.addAll(trackers.getTrackers());
        updateFilterList(filters);
    }

    @UiThread
    void updateFilterList(List<IssuesFilter> filters) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        filterListAdapter = new FilterListAdapter(getActivity(), R.layout.list_item_filter, R.id.base_layout, filters);
        filterList.setAdapter(filterListAdapter);
        filterListAdapter.notifyDataSetChanged();
        setHasOptionsMenu(true);
        selectFilter(0);
    }

    @ItemClick(R.id.filter_list)
    void selectFilter(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        filterList.setItemChecked(position, true);
        filterList.setSelection(position);
        IssuesFilter filter = filterListAdapter.getItem(position);
        if (getActivity() == null) return;
        ((NavigationCallBacks)getActivity()).onChangeFilter(filter);
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

    @ItemClick(R.id.account_list)
    void selectAccount(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountList.setItemChecked(position, true);
        accountList.setSelection(position);
        Account account = accountListAdapter.getItem(position);
        if (account.equals(app.getAccountManager().getEnableAccount())) return;
        app.getAccountManager().authenticate(account, this);
    }

    @Override @UiThread
    public void onAuthSuccessful(Account account) {
        if (getActivity() == null) return;
        ((NavigationCallBacks)getActivity()).onSwitchAccount(account);
    }

    @Override @UiThread
    public void onAuthFailed(Account account, int errorno, Exception e) {
        if (getActivity() == null) return;
        ((NavigationCallBacks) getActivity()).onStartAuthentication(account);
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

    public static NavigationFragment newInstance() {
        return NavigationFragment_.builder().build();
    }

    public interface NavigationCallBacks {
        void onChangeFilter(IssuesFilter filter);
        void onSwitchAccount(Account account);
        void onStartAuthentication(Account account);
    }

}
