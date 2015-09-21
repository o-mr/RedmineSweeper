package com.kz.redminesweeper.bean;

import java.io.Serializable;

public interface IssuesFilter extends ListItem, Serializable {

    public void setId(String id);

    public String getName();

    public void setName(String name);

    public int getColorId();
}
