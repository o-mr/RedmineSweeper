package com.kz.redminesweeper.bean;

import java.io.Serializable;
import java.util.List;

public class Trackers implements Serializable {

    private List<Tracker> trackers;

    public List<Tracker> getTrackers() {
        return trackers;
    }

    public void setTrackers(List<Tracker> trackers) {
        this.trackers = trackers;
    }

}
