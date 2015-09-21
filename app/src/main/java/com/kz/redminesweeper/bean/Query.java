package com.kz.redminesweeper.bean;

import com.kz.redminesweeper.R;

import java.io.Serializable;


public class Query implements IssuesFilter, Serializable {

    private String id;

    private String name;

    private int project_id;

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

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    @Override
    public int getColorId() {
        int offset = (Integer.parseInt(this.id) % 10) - 1;
        return R.color.tracker_0 + offset;
    }
}
