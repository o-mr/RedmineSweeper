package com.kz.redminesweeper.bean;

import java.io.Serializable;

public class CurrentUser implements Serializable {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
