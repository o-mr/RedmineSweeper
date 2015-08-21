package com.kz.redminesweeper.account;

import com.google.gson.annotations.Expose;
import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@EBean
public class Account implements Serializable, Cloneable {

    @Expose
    private int id = -1;

    @Expose
    private String rootUrl = "";

    @Expose
    private String loginId = "";

    // in memory
    private String password = "";

    @Expose
    private String _perpetuationPassword = "";

    @Expose
    private boolean enable;

    @Expose
    private boolean savePassword = true;

    @Expose
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        if (password.length() == 0) {
            return _perpetuationPassword;
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String get_perpetuationPassword() {
        return _perpetuationPassword;
    }

    public void set_perpetuationPassword(String _perpetuationPassword) {
        this._perpetuationPassword = _perpetuationPassword;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isSavePassword() {
        return savePassword;
    }

    public void setSavePassword(boolean savePassword) {
        this.savePassword = savePassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Account)) return false;
        return id == ((Account)obj).id;
    }

    @Override
    public String toString() {
        return loginId + "@" + rootUrl.replaceAll(".*//", "");
    }

    @Override
    public Account clone() {
        Account clone = new Account();
        clone.id = id;
        clone.rootUrl = rootUrl;
        clone.loginId = loginId;
        clone.password = password;
        clone._perpetuationPassword = _perpetuationPassword;
        clone.enable = enable;
        clone.savePassword = savePassword;
        clone.user = user;
        return clone;
    }

}
