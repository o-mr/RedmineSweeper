package com.kz.redminesweeper;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.kz.redminesweeper.adapter.IssueListPagerAdapter;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Tracker;
import com.kz.redminesweeper.fragment.NavigationFragment;
import com.kz.redminesweeper.rest.RedmineAuthInterceptor;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity implements NavigationFragment.FilterSelectedCallbacks {

    @ViewById
    ViewPager pager;

    @ViewById
    FrameLayout navigationFrame;

    @ViewById
    DrawerLayout drawerLayout;

    @App
    RmSApplication app;

    IssueListPagerAdapter issueListPagerAdapter;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        createNavigation();
        createActionBar();
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
        if (issueListPagerAdapter == null) {
            createIssueListPager();
        } else {
            issueListPagerAdapter.updateIssueList();
        }
    }
}
