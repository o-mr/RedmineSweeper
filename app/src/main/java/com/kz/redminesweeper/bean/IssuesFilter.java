package com.kz.redminesweeper.bean;

import java.io.Serializable;

public interface IssuesFilter extends Serializable {

    public String getId();

    public void setId(String id);

    public String getName();

    public void setName(String name);

    public String getLabel();

    public int getColorId();
}
