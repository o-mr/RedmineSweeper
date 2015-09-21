package com.kz.redminesweeper.bean;

public class Partition implements ListItem {

    private String title;

    public Partition(String title) {
        this.title = title;
    }

    public String getId() {
        return "";
    }

    public String getLabel() {
        return title;
    }
}