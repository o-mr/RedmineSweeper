package com.kz.redminesweeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.ListItem;
import com.kz.redminesweeper.bean.Partition;
import com.kz.redminesweeper.view.FilterListItem;
import com.kz.redminesweeper.view.FilterListItem_;
import com.kz.redminesweeper.view.ListPartition;
import com.kz.redminesweeper.view.ListPartition_;

import java.util.List;

public class FilterListAdapter extends ArrayAdapter<ListItem> {

    LayoutInflater layoutInflater;

    public FilterListAdapter(Context context, int layoutId, int viewId, List<ListItem> list)  {
        super(context, layoutId, viewId, list);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = getItem(position);
        if (isEnabled(position)) {
            if (convertView == null || !(convertView instanceof FilterListItem)) {
                convertView = FilterListItem_.build(getContext());
            }
            ((FilterListItem)convertView).bind((IssuesFilter)item);
        } else {
            if (convertView == null || !(convertView instanceof ListPartition)) {
                convertView = ListPartition_.build(getContext());
            }
            ((ListPartition)convertView).bind((Partition)item);
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof IssuesFilter;
    }

    public void addPartition(String title) {
        add(new Partition(title));
    }
}
