package com.kz.redminesweeper;

import android.content.Intent;
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

    @Override
    protected void onStart() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onStart();
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
        setTitle();
        if (issueListPagerAdapter == null) {
            createIssueListPager();
        } else {
            issueListPagerAdapter.updateIssueList(filter);
        }
    }

    public void setTitle() {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        getSupportActionBar().setTitle(app.getFilter().getName());
        Drawable drawable = getResources().getDrawable(app.getFilter().getColorId());
        getSupportActionBar().setBackgroundDrawable(drawable);
        int color = getResources().getColor(app.getFilter().getColorId());
        pagerTab.setTextColor(color);
        pagerTab.setTabIndicatorColor(color);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
