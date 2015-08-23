package com.kz.redminesweeper.bean;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.Date;

public class Issue implements Serializable {

    private int id;

    private Project project;

    private Tracker tracker;

    private Status status;

    private Priority priority;

    private User author;

    private User assigned_to;

    private Issue parent;

    private String subject;

    private String description;

    private int done_ratio;

    private Date start_date;

    private Date created_on;

    private Date updated_on;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(User assigned_to) {
        this.assigned_to = assigned_to;
    }

    public Issue getParent() {
        return parent;
    }

    public void setParent(Issue parent) {
        this.parent = parent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDone_ratio() {
        return done_ratio;
    }

    public void setDone_ratio(int done_ratio) {
        this.done_ratio = done_ratio;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public Date getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Date updated_on) {
        this.updated_on = updated_on;
    }

    public static String convertDateToString(Date date) {
        return DateFormat.format("yyyy-MM-dd kk:mm:ss", date).toString();
    }

    @Override
    public String toString() {
        return getId() + " " + getSubject();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other instanceof Issue) {
            return ((Issue)other).id == id;
        } else {
            return false;
        }
    }
}
