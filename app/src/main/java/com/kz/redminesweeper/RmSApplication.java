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
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@EApplication
public class RmSApplication extends Application {

    @RestService
    RedmineRestService redmine;

    @Bean
    RedmineAuthInterceptor authInterceptor;

    @Bean
    AccountManager accountManager;

    IssuesFilter filter;

    @AfterInject
    public void setUpAccountManager()     {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        accountManager.loadAccounts();
    }

    public void setUpRedmineRestService(Account account) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        redmine.setRootUrl(account.getRootUrl());
        authInterceptor.setAccount(account);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(authInterceptor);
        RestTemplate template = redmine.getRestTemplate();
        template.setInterceptors(interceptors);
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public RedmineRestService getRedmine() {
        return redmine;
    }

    public IssuesFilter getFilter() {
        return filter;
    }

    public void setFilter(IssuesFilter filter) {
        this.filter = filter;
    }


}