package com.kz.redminesweeper.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Queries implements Serializable, Iterable<Query> {

    List<Query> queries;

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public Query getQuery(String id) {
        for (Query query : queries) {
            if (query.getId().equals(id)) return query;
        }
        return null;
    }

    public void sort() {
        Collections.sort(queries, new Comparator<Query>() {
            @Override
            public int compare(Query lhs, Query rhs) {
                return lhs.getId().compareTo(rhs.getId());
            }
        });
    }

    @Override
    public Iterator<Query> iterator() {
        return queries.iterator();
    }
}
