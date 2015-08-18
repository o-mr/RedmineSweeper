package com.kz.redminesweeper;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kz.redminesweeper.bean.Account;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.User;
import com.kz.redminesweeper.prefs.RmSPrefs_;
import com.kz.redminesweeper.rest.RedmineAuthInterceptor;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
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

    @Pref
    RmSPrefs_ prefs;

    IssuesFilter filter;

    ArrayList<Account> accounts;

    @Override
    public void onCreate() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onCreate();
        loadAccounts();
    }

    public void loadAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String accountsJson = prefs.getAccountsJson().get();
        Log.e(getClass().getName(), accountsJson);
        try {
            accounts = new Gson().fromJson(accountsJson, new TypeToken<ArrayList<Account>>(){}.getType());
            accounts.size(); //NPE
        } catch (Exception e) {
            accounts = new ArrayList<>();
        }
    }

    public void saveAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String accountsJson = new Gson().toJson(accounts, new TypeToken<ArrayList<Account>>() {
        }.getType());
        Log.e(getClass().getName(), accountsJson);
        prefs.getAccountsJson().put(accountsJson);
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

    public RedmineRestService getRedmine() {
        return redmine;
    }

    public IssuesFilter getFilter() {
        return filter;
    }

    public void setFilter(IssuesFilter filter) {
        this.filter = filter;
    }

    public void addAccount(Account account) {
        int index = accounts.indexOf(account);
        if (index < 0) {
            accounts.add(account);
        } else {
            accounts.set(index, account);
        }
    }

    public void removeAccount(Account account) {
        accounts.add(account);
    }

    public Account getEnableAccount() {
        for (Account account : accounts) {
            if (account.isEnable()) return account;
        }
        return null;
    }

    public Account getAccount() {
        return getEnableAccount();
    }

    public void setUser(User user) {
        getEnableAccount().setUser(user);
    }

}
