package com.kz.redminesweeper.prefs;

import com.kz.redminesweeper.bean.Account;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface RmSPrefs {

    @DefaultString("")
    String getAccountsJson();

}
