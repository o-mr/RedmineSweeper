package com.kz.redminesweeper;

import android.app.Application;
import android.util.Log;

import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.bean.Projects;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.Statuses;
import com.kz.redminesweeper.bean.Tracker;
import com.kz.redminesweeper.bean.Trackers;
import com.kz.redminesweeper.rest.RedmineAccess;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import java.util.ArrayList;
import java.util.List;

@EApplication
public class RmSApplication extends Application {

    @Bean
    AccountManager accountManager;

    @Bean
    RedmineAccess redmineAccess;

    private Statuses statuses;

    private ActivityErrorReceiver errorReceiver;

    @AfterInject
    public void setUpAccountManager()     {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountManager.loadAccounts();
    }

    public void setErrorReceiver(ActivityErrorReceiver errorReceiver) {
        this.errorReceiver = errorReceiver;
    }

    public void downloadStatuses() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        redmineAccess.downloadStatuses(new RedmineAccess.RestResultListener<Statuses>() {
            @Override
            public void onSuccessful(Statuses result) {
                statuses = result;
            }

            @Override
            public void onFailed(int msgId, Throwable e) {
                if (errorReceiver != null) errorReceiver.onReceivedError(msgId, e);
            }
        });
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RedmineAccess getRedmine() {
        return redmineAccess;
    }

    public Statuses getStatuses() {
        return statuses;
    }
}