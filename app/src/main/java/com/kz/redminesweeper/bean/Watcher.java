package com.kz.redminesweeper.bean;

import com.kz.redminesweeper.R;

import java.io.Serializable;

public class Watcher implements IssuesFilter, Serializable {

    private String id;

    private String name;

    public Watcher() {}

    public Watcher(String id, String name) {
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
        if ("me".equals(id)) {
            return "W";
        } else {
            return id;
        }
    }

    @Override
    public int getColorId() {
        return R.color.status_watch;
    }

}
