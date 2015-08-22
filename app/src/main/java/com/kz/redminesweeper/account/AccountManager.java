package com.kz.redminesweeper.account;

import android.content.Context;
import android.util.Log;

import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kz.redminesweeper.bean.User;
import com.kz.redminesweeper.prefs.SharedPreferences_;
import com.kz.redminesweeper.rest.RedmineRestHelper;
import com.kz.redminesweeper.rest.RedmineRestService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class AccountManager {

    @Bean
    RedmineRestHelper redmineRestHelper;

    @Pref
    SharedPreferences_ prefs;

    @RootContext
    Context context;

    private Crypto crypto;

    private List<Account> accounts;

    private static int idSeq;

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private static final Type JSON_OBJECT_TYPE = new TypeToken<ArrayList<Account>>() {}.getType();

    private static final String ENTITY_NAME = "com.kz.redminesweeper.crypto";

    public void loadAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        try {
            crypto = new Crypto(new SharedPrefsBackedKeyChain(context), new SystemNativeCryptoLibrary());
            String encryptedToString = prefs.getAccountsJson().get();
            String[] split = encryptedToString.substring(1, encryptedToString.length() - 1).split(", ");
            byte[] encrypted = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                encrypted[i] = Byte.parseByte(split[i]);
            }
            byte[] decrypted = crypto.decrypt(encrypted, new Entity(ENTITY_NAME));
            accounts = GSON.fromJson(new String(decrypted, "utf-8"), JSON_OBJECT_TYPE);
            for (Account account : accounts) {
                idSeq = Math.max(account.getId(), idSeq);
            }
        } catch (Exception e) {
            accounts = new ArrayList<>();
        }
    }

    public void authenticate(final Account account, final AccountAuthenticator authenticator) {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        if (account.getPassword().length() == 0) {
            authenticator.onAuthFailed(account, 1, 1, null);
            return;
        }
        final Account bk = getEnableAccount();
        redmineRestHelper.setUpRedmineRestService(account);
        redmineRestHelper.executeRest(new RedmineRestHelper.RestExecutor<User>() {
            @Override
            public User execute(RedmineRestService redmine) {
                if (account.getPassword() == null) {
                    return null;
                } else if (!account.isAuthenticated()) {
                    return redmine.getMyUserInfo().getUser();
                } else {
                    return account.getUser();
                }
            }

            @Override
            public void onSuccessful(User result) {
                account.setUser(result);
                changeEnableAccount(account);
                authenticator.onAuthSuccessful(account);
            }

            @Override
            public void onFailed(RedmineRestHelper.RestError restError, int msgId, Throwable e) {
                redmineRestHelper.setUpRedmineRestService(bk);
                authenticator.onAuthFailed(account, 1, msgId, e);
            }
        });

    }

    private void saveAccounts() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        String decrypted = GSON.toJson(accounts, JSON_OBJECT_TYPE);
        try {
            byte[] encrypted = crypto.encrypt(decrypted.getBytes("utf-8"), new Entity(ENTITY_NAME));
            prefs.getAccountsJson().put(Arrays.toString(encrypted));
        } catch (Exception e) {
            Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
        }
    }

    private void putAccount(Account account) {
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

    public void removeAccount(Account account) {
        accounts.remove(account);
        if (account.isEnable()) {
            accounts.get(0).setEnable(true);
        }
        saveAccounts();
    }

    private void changeEnableAccount(Account account) {
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
        void onAuthFailed(Account account, int errorNo, int msgId, Throwable e);
    }
}
