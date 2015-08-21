package com.kz.redminesweeper.account;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kz.redminesweeper.prefs.SharedPreferences_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class AccountManager {

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

    private synchronized void saveAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String accountsJson = GSON.toJson(accounts, JSON_OBJECT_TYPE);
        Log.e(getClass().getName(), accountsJson);
        prefs.getAccountsJson().put(accountsJson);
    }

    public synchronized void putAccount(Account account) {
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

    public synchronized void changeEnableAccount(Account account) {
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
}
