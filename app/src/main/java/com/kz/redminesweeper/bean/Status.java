package com.kz.redminesweeper.bean;

import java.io.Serializable;

public class Status implements IssuesFilter, Serializable {

    private String id;

    private String name;

    public Status() {}

    public Status(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        if ("*".equals(id)) {
            return "A";
        } else {
            return getId().toUpperCase();
        }
    }

}
