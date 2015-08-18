package com.kz.redminesweeper;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.kz.redminesweeper.adapter.IssueListPagerAdapter;
import com.kz.redminesweeper.bean.Account;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.fragment.NavigationFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity implements NavigationFragment.FilterSelectedCallbacks {

    @App
    RmSApplication app;

    @ViewById
    DrawerLayout drawerLayout;

    @ViewById
    FrameLayout navigationFrame;

    @ViewById
    ViewPager pager;

    @ViewById
    PagerTabStrip pagerTab;

    IssueListPagerAdapter issueListPagerAdapter;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Account account = app.getAccount();
        if (account == null) {

        }
        app.setUpRedmineRestService(account);
        createIssueListPager();
        createActionBar();
        createNavigation();
    }

    void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    void createNavigation() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTran = fManager.beginTransaction();
        NavigationFragment navigationFragment = NavigationFragment.newInstance();
        fTran.replace(R.id.navigation_frame, navigationFragment);
        navigationFragment.setDrawer(drawerLayout, navigationFrame);
        fTran.commit();
    }

    @Background
    void createIssueListPager() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        List<Project> projects = app.getRedmine().getProjects().getProjects();
        setUpIssueListPager(projects);
    }

    @UiThread
    void setUpIssueListPager(List<Project> projects) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        issueListPagerAdapter = new IssueListPagerAdapter(getSupportFragmentManager(), pager, projects);
        pager.setAdapter(issueListPagerAdapter);
    }

    @Override
    public void onFilterSelected(IssuesFilter filter) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        getSupportActionBar().setTitle(app.getFilter().getName());
        Drawable drawable = getResources().getDrawable(filter.getColorId());
        getSupportActionBar().setBackgroundDrawable(drawable);
        int color = getResources().getColor(filter.getColorId());
        pagerTab.setTextColor(color);
        pagerTab.setTabIndicatorColor(color);
        if (issueListPagerAdapter == null) {
            createIssueListPager();
        } else {
            issueListPagerAdapter.updateIssueList(filter);
        }
    }
}
