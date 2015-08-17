package com.kz.redminesweeper.bean;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@EBean
public class Account implements Serializable {

    private String rootUrl;

    private User user;

    private String loginId;

    private String password;

    private boolean enable;

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
