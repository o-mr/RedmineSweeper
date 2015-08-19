package com.kz.redminesweeper.account;

import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@EBean
public class Account implements Serializable {

    private String rootUrl = "";

    private String loginId = "";

    private String password = "";

    private boolean enable;

    private boolean savePassword = true;

    private User user;

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
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return obj.toString().equals(toString());
    }

    @Override
    public String toString() {
        return loginId + "@" + rootUrl.replaceAll(".*//", "");
    }

}
