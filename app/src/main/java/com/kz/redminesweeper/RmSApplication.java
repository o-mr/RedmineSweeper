package com.kz.redminesweeper;

import android.app.Application;
import android.util.Log;

import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.rest.RedmineRestHelper;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

@EApplication
public class RmSApplication extends Application {

    @Bean
    AccountManager accountManager;

    @Bean
    RedmineRestHelper redmineRestHelper;

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


}