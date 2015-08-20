package com.kz.redminesweeper;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.adapter.IssueListPagerAdapter;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.bean.Issues;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.fragment.IssueListFragment;
import com.kz.redminesweeper.fragment.NavigationFragment;
import com.kz.redminesweeper.view.BlankWall;
import com.kz.redminesweeper.view.BlankWall_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;

import java.util.List;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity implements NavigationFragment.FilterSelectedCallbacks, IssueListFragment.LoadedIssuesCallbacks {

    @App
    RmSApplication app;

    @ViewById
    DrawerLayout baseLayout;

    @ViewById
    FrameLayout navigationFrame;

    @ViewById
    ViewPager pager;

    @ViewById
    PagerTabStrip pagerTab;

    IssueListPagerAdapter issueListPagerAdapter;

    @AfterViews
    public void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Account account = app.getAccountManager().getEnableAccount();
        app.setUpRedmineRestService(account);
        createIssueListPager();
        createNavigation();
        createActionBar();
    }

    void createActionBar() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().show();
    }

    void createNavigation() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        baseLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTran = fManager.beginTransaction();
        NavigationFragment navigationFragment = NavigationFragment.newInstance();
        navigationFragment.setDrawer(baseLayout, navigationFrame);
        fTran.replace(R.id.navigation_frame, navigationFragment);
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
        BackgroundExecutor.cancelAll("", true);
        setTitle();
        if (issueListPagerAdapter == null) {
            createIssueListPager();
        } else {
            issueListPagerAdapter.updateIssueList(filter);
        }
    }

    @Override
    public void onLoadedIssues(Fragment fragment, Issues issues) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
    }

    public void setTitle() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        getSupportActionBar().setTitle(app.getFilter().getName());
        Drawable drawable = getDrawableById(app.getFilter().getColorId());
        getSupportActionBar().setBackgroundDrawable(drawable);
        int color = getResources().getColor(app.getFilter().getColorId());
        pagerTab.setTextColor(color);
        pagerTab.setTabIndicatorColor(color);
    }

    public void reboot() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        BackgroundExecutor.cancelAll("", true);
        app.setFilter(null);
        setUp();
    }

    @SuppressWarnings("deprecation")
    public Drawable getDrawableById(int id) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }

    public void startAccountSettings(Account account, AccountSettingsActivity.Mode mode) {
        Intent intent = new Intent(MainActivity.this, AccountSettingsActivity_.class);
        intent.putExtra("account", account);
        intent.putExtra("modeInt", mode.ordinal());
        startActivity(intent);
        // TODO reboot は AccountSettings で行う。
    }

    public void onActivityResult( int requestCode, int resultCode, Intent intent ) {
        if (requestCode == 1) {
            setUp();
        }
    }
}
