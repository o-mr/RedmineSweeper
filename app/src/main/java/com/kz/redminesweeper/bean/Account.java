package com.kz.redminesweeper.bean;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;

@EBean
public class Account implements Serializable {

    private String rootUrl;

    private User user;

    private String loginId;

    private String password;

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

}
