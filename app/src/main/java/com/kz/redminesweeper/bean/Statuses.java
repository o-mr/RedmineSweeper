package com.kz.redminesweeper.bean;

import java.io.Serializable;
import java.util.List;

public class Statuses implements Serializable {

    List<Status> issue_statuses;

    public List<Status> getIssue_statuses() {
        return issue_statuses;
    }

    public void setIssue_statuses(List<Status> issue_statuses) {
        this.issue_statuses = issue_statuses;
    }

}
