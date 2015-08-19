package com.kz.redminesweeper.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.content.res.Configuration;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.kz.redminesweeper.MainActivity;
import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.adapter.AccountListAdapter;
import com.kz.redminesweeper.adapter.FilterListAdapter;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.bean.Watcher;
import com.kz.redminesweeper.view.AccountHeader;
import com.kz.redminesweeper.view.NavigationToggle;

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

    private FilterListAdapter filterListAdapter;

    private AccountListAdapter accountListAdapter;

    private NavigationToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;

    private FrameLayout mNavigationFrame;

    @Override
    public void onAttach(Activity activity) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onAttach(activity);
        mDrawerToggle = new NavigationToggle(getActivity(), mDrawerLayout);
    }

    @AfterViews
    public void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountHeader.bind(app.getAccountManager().getEnableAccount());
        createFilterList();
        createAccountList();
    }

    public void setDrawer(DrawerLayout drawerLayout, FrameLayout navigationFrame) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        mDrawerLayout = drawerLayout;
        mNavigationFrame = navigationFrame;
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
        selectFilter(0);
        setHasOptionsMenu(true);
    }

    void createAccountList() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountListAdapter = new AccountListAdapter(getActivity(), R.layout.list_item_account, R.id.base_layout, app.getAccountManager().getAccounts());
        accountList.setAdapter(accountListAdapter);
        accountListAdapter.notifyDataSetChanged();
        selectAccount(app.getAccountManager().indexOfEnableAccount());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @ItemClick(R.id.filter_list)
    void selectFilter(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        mDrawerLayout.closeDrawer(mNavigationFrame);
        filterList.setItemChecked(position, true);
        filterList.setSelection(position);
        IssuesFilter filter = filterListAdapter.getItem(position);
        app.setFilter(filter);
        ((FilterSelectedCallbacks)getActivity()).onFilterSelected(filter);
    }

    @ItemClick(R.id.account_list)
    void selectAccount(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Account account = accountListAdapter.getItem(position);
        mDrawerLayout.closeDrawer(mNavigationFrame);
        accountList.setItemChecked(position, true);
        accountList.setSelection(position);
        if (account.equals(app.getAccountManager().getEnableAccount())) return;
        app.getAccountManager().changeEnableAccount(account);
        ((MainActivity)getActivity()).reboot();
    }

    @Click(R.id.account_header)
    void showAccountSettings() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public static NavigationFragment newInstance() {
        return NavigationFragment_.builder().build();
    }

    public interface FilterSelectedCallbacks {
        void onFilterSelected(IssuesFilter filter);
    }

}
