package com.kz.redminesweeper;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.adapter.IssueListPagerAdapter;
import com.kz.redminesweeper.bean.Issues;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Projects;
import com.kz.redminesweeper.fragment.IssueListFragment;
import com.kz.redminesweeper.fragment.NavigationFragment;
import com.kz.redminesweeper.rest.RedmineAccess;
import com.kz.redminesweeper.view.AccountListItem;
import com.kz.redminesweeper.view.NavigationToggle;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;


@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity
        implements NavigationFragment.NavigationCallBacks, IssueListFragment.IssueListCallbacks, AccountListItem.AccountListItemListener, ActivityErrorReceiver {

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

    private NavigationToggle navigationToggle;

    private boolean setupCompleted;

    private IssueListPagerAdapter issueListPagerAdapter;

    @Override
    protected void onStart() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onStart();
        setUp();
    }

    private void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (setupCompleted) return;
        app.setErrorReceiver(this);
        app.downloadStatuses();
        createIssueListPager();
        createActionBar();
        setupCompleted = true;
    }

    void createIssueListPager() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        app.getRedmine().downloadProjects(new RedmineAccess.RestResultListener<Projects>() {
            @Override
            public void onSuccessful(Projects result) {
                updateIssueListPager(result);
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                onReceivedError(msgId, e);
            }
        });
    }

    @UiThread
    void updateIssueListPager(Projects projects) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        issueListPagerAdapter = new IssueListPagerAdapter(getSupportFragmentManager(), pager, projects.getProjects(), this);
        pager.setAdapter(issueListPagerAdapter);
        createNavigation();
    }

    void createActionBar() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setElevation(0f);
        actionBar.show();
    }

    void createNavigation() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        navigationToggle = new NavigationToggle(this, baseLayout);
        baseLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTran = fManager.beginTransaction();
        NavigationFragment navigationFragment = NavigationFragment.newInstance();
        navigationFragment.setNavigationCallBacks(this);
        navigationFragment.setErrorReceiver(this);
        fTran.replace(R.id.navigation_frame, navigationFragment);
        fTran.commit();
    }

    @Override
    public void onChangeFilter(IssuesFilter filter) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        baseLayout.closeDrawer(navigationFrame);
        BackgroundExecutor.cancelAll("", true);
        setTheme(filter);
        issueListPagerAdapter.updateIssueList(filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        return navigationToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwitchAccount(Account account) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        refresh();
    }

    @Override
    public void onStartAuthentication(Account account) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        startAccountSettings(account, AccountSettingsActivity.Mode.SWITCH, 0);
    }

    @Override
    public void onEditAccount(Account account) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        startAccountSettings(account, AccountSettingsActivity.Mode.EDIT, 0);
    }

    @Override
    public void onLoadedIssues(Fragment fragment, Issues issues) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        // TODO
    }

    @Override
    public void onReceivedError(int msgId, Throwable e) {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
        finish();
        startAccountSettings(app.getAccountManager().getEnableAccount(), AccountSettingsActivity.Mode.ERROR, msgId);
    }

    public void setTheme(IssuesFilter filter) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(filter.getName());
        Drawable drawable = getDrawableById(filter.getColorId());
        actionBar.setBackgroundDrawable(drawable);
        int color = getResources().getColor(filter.getColorId());
        pagerTab.setBackgroundColor(color);
        baseLayout.setBackgroundColor(color);
    }

    public void refresh() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        baseLayout.closeDrawer(navigationFrame);
        issueListPagerAdapter = null;
        BackgroundExecutor.cancelAll("", true);
        setupCompleted = false;
        setUp();
    }

    public void startAccountSettings(Account account, AccountSettingsActivity.Mode mode, int msgId) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Intent intent = new Intent(MainActivity.this, AccountSettingsActivity_.class);
        intent.putExtra("msgId", msgId);
        intent.putExtra("account", account);
        intent.putExtra("modeInt", mode.ordinal());
        startActivityForResult(intent, AccountSettingsActivity.REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == AccountSettingsActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setupCompleted = false;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public Drawable getDrawableById(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }
}
