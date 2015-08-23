package com.kz.redminesweeper.bean;

import com.kz.redminesweeper.R;

import java.io.Serializable;

public class Status implements IssuesFilter, Serializable {

    private String id;

    private String name;

    private boolean is_closed;

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

    public boolean is_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public String getLabel() {
        if ("*".equals(id)) {
            return "A";
        } else {
            return getId().toUpperCase();
        }
    }

    @Override
    public int getColorId() {
        switch (id) {
            case "c" : return R.color.status_close;
            case "o" : return R.color.status_open;
            case "*" : return R.color.status_all;
            default  : return R.color.theme;
        }
    }
}
