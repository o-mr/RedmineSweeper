package com.kz.redminesweeper;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.adapter.IssueListPagerAdapter;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.fragment.NavigationFragment;

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

    private boolean setupCompleted;

    IssueListPagerAdapter issueListPagerAdapter;

    public void setUp() {
        if (setupCompleted) return;
        Account account = app.getAccountManager().getEnableAccount();
        if (account == null) account = new Account();
        if (account.getPassword().length() == 0) {
            Intent intent = new Intent(MainActivity.this, AccountSettingsActivity_.class);
            intent.putExtra("account", account);
            startActivity(intent);
        } else {
            app.setUpRedmineRestService(account);
            createIssueListPager();
            createActionBar();
            createNavigation();
            setupCompleted = true;
        }
    }

    @Override
    protected void onStart() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onStart();
        setUp();
    }

    void createActionBar() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
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
        navigationFragment.setDrawer(drawerLayout, navigationFrame);
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

    public void setTitle() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        getSupportActionBar().setTitle(app.getFilter().getName());
        Drawable drawable = getDrawableById(app.getFilter().getColorId());
        getSupportActionBar().setBackgroundDrawable(drawable);
        int color = getResources().getColor(app.getFilter().getColorId());
        pagerTab.setTextColor(color);
        pagerTab.setTabIndicatorColor(color);
    }

    private boolean reboot;

    public void reboot() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        BackgroundExecutor.cancelAll("", true);
        app.setFilter(null);
        setupCompleted = false;
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

}
