package com.kz.redminesweeper.account;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kz.redminesweeper.bean.User;
import com.kz.redminesweeper.prefs.SharedPreferences_;
import com.kz.redminesweeper.rest.RedmineAuthInterceptor;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class AccountManager {

    @RestService
    RedmineRestService redmine;

    @Bean
    RedmineAuthInterceptor authInterceptor;

    List<Account> accounts;

    @Pref
    SharedPreferences_ prefs;

    private static int idSeq;

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private static final Type JSON_OBJECT_TYPE = new TypeToken<ArrayList<Account>>() {}.getType();

    public void loadAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String accountsJson = prefs.getAccountsJson().get();
        Log.e(getClass().getName(), accountsJson);
        try {
            accounts = GSON.fromJson(accountsJson, JSON_OBJECT_TYPE);
            for (Account account : accounts) {
                idSeq = Math.max(account.getId(), idSeq);
            }
        } catch (Exception e) {
            accounts = new ArrayList<>();
        }
    }

    public RedmineRestService getRedmine() {
        return redmine;
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

    @Background
    public void authenticate(Account account, AccountAuthenticator authenticator) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        Account bk = getEnableAccount();
        setUpRedmineRestService(account);
        try {
            account.getPassword().length(); //NPE
            if (!account.isAuthenticated()) {
                User user = redmine.getMyUserInfo().getUser();
                user.getName().length(); //NPE
                account.setUser(user);
            }
            changeEnableAccount(account);
            authenticator.onAuthSuccessful(account);
        } catch (Exception e) {
            setUpRedmineRestService(bk);
            authenticator.onAuthFailed(account, 1, e);
        }
    }

    private synchronized void saveAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String accountsJson = GSON.toJson(accounts, JSON_OBJECT_TYPE);
        Log.e(getClass().getName(), accountsJson);
        prefs.getAccountsJson().put(accountsJson);
    }

    private synchronized void putAccount(Account account) {
        if (account.isSavePassword()) {
            account.set_perpetuationPassword(account.getPassword());
        } else {
            account.set_perpetuationPassword("");
        }
        int index = accounts.indexOf(account);
        if (index < 0) {
            account.setId(++idSeq);
            accounts.add(account);
        } else {
            accounts.set(index, account);
        }
        saveAccounts();
    }

    public synchronized void removeAccount(Account account) {
        accounts.remove(account);
        if (account.isEnable()) {
            accounts.get(0).setEnable(true);
        }
        saveAccounts();
    }

    private synchronized void changeEnableAccount(Account account) {
        for (Account a : accounts) {
            a.setEnable(false);
        }
        account.setEnable(true);
        putAccount(account);
    }

    public synchronized Account getEnableAccount() {
        if (accounts.size() == 0) return null;
        for (Account account : accounts) {
            if (account.isEnable()) return account;
        }
        return accounts.get(0);
    }

    public synchronized int indexOfEnableAccount() {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).isEnable()) return i;
        }
        return -1;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public interface AccountAuthenticator {
        void onAuthSuccessful(Account account);
        void onAuthFailed(Account account, int errorno, Exception e);
    }
}
