package com.kz.redminesweeper;

import android.app.Application;
import android.util.Log;

import com.kz.redminesweeper.bean.Account;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Status;
import com.kz.redminesweeper.bean.User;
import com.kz.redminesweeper.rest.RedmineAuthInterceptor;
import com.kz.redminesweeper.rest.RedmineRestService;

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
    Account account;

    IssuesFilter filter;

    @Override
    public void onCreate() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        super.onCreate();
        // TODO
        account.setRootUrl("http://192.168.11.4/redmine");
        account.setLoginId("oomura");
        account.setPassword("password01");
        setUpRedmineRestService();
    }

    public void setUpRedmineRestService() {
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

    public void setRedmine(RedmineRestService redmine) {
        this.redmine = redmine;
    }

    public IssuesFilter getFilter() {
        return filter;
    }

    public void setFilter(IssuesFilter filter) {
        this.filter = filter;
    }

    public Account getAccount() {
        return account;
    }

    public void setUser(User user) {
        account.setUser(user);
    }

}
