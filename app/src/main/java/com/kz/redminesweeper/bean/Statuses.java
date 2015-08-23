package com.kz.redminesweeper.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class Statuses implements Serializable, Iterable<Status> {

    List<Status> issue_statuses;

    public List<Status> getIssue_statuses() {
        return issue_statuses;
    }

    public void setIssue_statuses(List<Status> issue_statuses) {
        this.issue_statuses = issue_statuses;
    }

    public Status getStatus(String id) {
        for (Status status : issue_statuses) {
            if (status.getId().equals(id)) return status;
        }
        return null;
    }

    public boolean isCloseIssue(Issue issue) {
        Status status = getStatus(issue.getStatus().getId());
        return status != null && status.is_closed();
    }

    @Override
    public Iterator<Status> iterator() {
        return issue_statuses.iterator();
    }
}
