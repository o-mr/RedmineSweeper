package com.kz.redminesweeper.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Journal implements Serializable {

    private int id;

    private User user;

    private String notes;

    private Date created_on;

    private List<Detail> details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public class Detail implements Serializable {

        private String property;

        private String name;

        private String old_value;

        private String new_value;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOld_value() {
            return old_value;
        }

        public void setOld_value(String old_value) {
            this.old_value = old_value;
        }

        public String getNew_value() {
            return new_value;
        }

        public void setNew_value(String new_value) {
            this.new_value = new_value;
        }



    }

}
