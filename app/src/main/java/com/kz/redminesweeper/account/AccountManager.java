package com.kz.redminesweeper.account;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kz.redminesweeper.prefs.SharedPreferences_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class AccountManager {

    List<Account> accounts;

    @Pref
    SharedPreferences_ prefs;

    public void loadAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String accountsJson = prefs.getAccountsJson().get();
        Log.e(getClass().getName(), accountsJson);
        try {
            accounts = new Gson().fromJson(accountsJson, new TypeToken<ArrayList<Account>>(){}.getType());
            accounts.size(); //NPE
        } catch (Exception e) {
            Log.e(getClass().getName(), accountsJson, e);
            accounts = new ArrayList<>();
        }
    }

    private synchronized void saveAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String accountsJson = new Gson().toJson(accounts, new TypeToken<ArrayList<Account>>() {
        }.getType());
        Log.e(getClass().getName(), accountsJson);
        prefs.getAccountsJson().put(accountsJson);
    }

    public synchronized void putAccount(Account account) {
        int index = accounts.indexOf(account);
        if (index < 0) {
            accounts.add(account);
        } else {
            accounts.set(index, account);
        }
        saveAccounts();
    }

    public synchronized void removeAccount(Account account) {
        accounts.remove(account);
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
        for (Account account : accounts) {
            if (account.isEnable()) return account;
        }
        return null;
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
