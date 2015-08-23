package com.kz.redminesweeper;

import android.app.Application;
import android.util.Log;

import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.bean.Issue;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Statuses;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.rest.RedmineRestHelper;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import java.util.List;

@EApplication
public class RmSApplication extends Application {

    @Bean
    AccountManager accountManager;

    @Bean
    RedmineRestHelper redmineRestHelper;

    private Statuses statuses;

    private ActivityErrorReceiver errorReceiver;

    @AfterInject
    public void setUpAccountManager()     {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountManager.loadAccounts();
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RedmineRestHelper getRedmine() {
        return redmineRestHelper;
    }

    @Background
    void downloadStatuses() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        getRedmine().executeRest(new RedmineRestHelper.RestExecutor<Statuses>() {
            @Override
            public Statuses execute(RedmineRestService redmine) {
                return redmine.getStatuses();
            }

            @Override
            public void onSuccessful(Statuses result) {
                statuses = result;
            }

            @Override
            public void onFailed(RedmineRestHelper.RestError restError, int msgId, Throwable e) {
                if (errorReceiver != null) errorReceiver.onReceivedError(msgId, e);
            }
        });
    }

    public void setErrorReceiver(ActivityErrorReceiver errorReceiver) {
        this.errorReceiver = errorReceiver;
    }

    public Statuses getStatuses() {
        return statuses;
    }
}