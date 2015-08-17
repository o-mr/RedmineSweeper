package com.kz.redminesweeper.bean;

import com.kz.redminesweeper.R;

import java.io.Serializable;

public class Tracker implements IssuesFilter, Serializable {

    private String id;

    private String name;

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
        return getId();
    }

    @Override
    public int getColorId() {
        int offset = (Integer.parseInt(this.id) % 10) - 1;
        return R.color.tracker_0 + offset;
    }
}
