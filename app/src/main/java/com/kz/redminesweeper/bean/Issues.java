package com.kz.redminesweeper.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class Issues implements Serializable, Iterable<Issue> {

    private List<Issue> issues;

    private int total_count;

    private int offset;

    private int limit;

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void nextOffset() {
        offset += limit;
    }

    @Override
    public Iterator<Issue> iterator() {
        return issues.iterator();
    }
}
