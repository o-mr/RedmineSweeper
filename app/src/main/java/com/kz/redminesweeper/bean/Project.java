package com.kz.redminesweeper.bean;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.Date;

public class Project implements Serializable {

    private int id;

    private String name;

    private String description;

    private int status;

    private Date created_on;

    private Date updated_on;

    public static String convertDateToString(Date date) {
        return DateFormat.format("yyyy-MM-dd kk:mm:ss", date).toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public String getCreatedOn() {
        return convertDateToString(created_on);
    }

    public Date getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Date updated_on) {
        this.updated_on = updated_on;
    }

    public String getUpdatedOn() {
        return convertDateToString(updated_on);
    }

    @Override
    public String toString() {
        return getId() + " " + getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other instanceof Project) {
            return ((Project)other).id == id;
        } else {
            return false;
        }
    }

}
