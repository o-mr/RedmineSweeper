package com.kz.redminesweeper;

import android.app.Application;
import android.util.Log;

import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.account.AccountManager;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.rest.RedmineAuthInterceptor;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

@EApplication
public class RmSApplication extends Application {

    @Bean
    AccountManager accountManager;

    IssuesFilter filter;

    @AfterInject
    public void setUpAccountManager()     {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountManager.loadAccounts();
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RedmineRestService getRedmine() {
        return accountManager.getRedmine();
    }

    public IssuesFilter getFilter() {
        return filter;
    }

    public void setFilter(IssuesFilter filter) {
        this.filter = filter;
    }


}