package com.kz.redminesweeper.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.adapter.FilterListAdapter;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.bean.User;
import com.kz.redminesweeper.bean.Watcher;
import com.kz.redminesweeper.view.AccountNavigation;
import com.kz.redminesweeper.view.AccountNavigation_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
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
    ListView filterList;

    private AccountNavigation accountNavigation;

    private FilterSelectedCallbacks filterSelectedCallbacks;

    private FilterListAdapter filterListAdapter;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;

    private FrameLayout mNavigationFrame;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountNavigation = AccountNavigation_.build(getActivity());
        filterList.addHeaderView(accountNavigation, null, false);
        accountNavigation.bind(app.getAccount());
        downloadFilter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setDrawer(DrawerLayout drawerLayout, FrameLayout navigationFrame) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        mDrawerLayout = drawerLayout;
        mNavigationFrame = navigationFrame;
    }

//    @Background
//    void downloadUserInfo() {
//        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
//        User user = app.getRedmine().getMyUserInfo().getUser();
//        setUserInfo(user);
//    }

    @UiThread
    void setUserInfo(User user) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        app.setUser(user);
        accountNavigation.bind(app.getAccount());
    }

    @Background
    void downloadFilter() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        List<IssuesFilter> filters = new ArrayList<>();
        filters.add(new Status("o", getString(R.string.filter_open)));
        filters.add(new Status("c", getString(R.string.filter_close)));
        filters.add(new Watcher("me", getString(R.string.filter_watch)));
        filters.add(new Status("*", getString(R.string.filter_all)));
        Trackers trackers = app.getRedmine().getTrackers();
        filters.addAll(trackers.getTrackers());
        updateFilterList(filters);
    }

    @UiThread
    void updateFilterList(List<? extends IssuesFilter> filter) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        filterListAdapter = new FilterListAdapter(getActivity(), R.layout.filter_list_item, R.id.base_layout, new ArrayList<IssuesFilter>());
        filterList.setAdapter(filterListAdapter);
        filterListAdapter.addAll(filter);
        filterListAdapter.notifyDataSetChanged();
        changeFilter(1);
    }

    public boolean isDrawerOpen() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mNavigationFrame);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onAttach(activity);
         filterSelectedCallbacks = (FilterSelectedCallbacks) activity;

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onDetach() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onDetach();
        filterSelectedCallbacks = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.main, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @ItemClick(R.id.filter_list)
    void statusListItemClicked(int position) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (position == 0) {

        } else {
            changeFilter(position);
        }
    }

    void changeFilter(int position) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mNavigationFrame);
        }
        if (filterList != null) {
            filterList.setItemChecked(position, true);
            filterList.setSelection(position);
        }
        IssuesFilter filter = filterListAdapter.getItem(position - 1);
        app.setFilter(filter);
        if (filterSelectedCallbacks != null) {
            filterSelectedCallbacks.onFilterSelected(filter);
        }
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
