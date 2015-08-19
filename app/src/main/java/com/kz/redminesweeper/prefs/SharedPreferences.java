package com.kz.redminesweeper.prefs;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface SharedPreferences {

    @DefaultString("")
    String getAccountsJson();

}
