package com.kz.redminesweeper.bean;

import java.io.Serializable;

public class User implements Serializable {

    private int id;

    private String login;

    private String name;

    private String firstname;

    private String lastname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        if (name == null) {
            return firstname + " " + lastname;
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
