package com.kz.redminesweeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.view.FilterListItem;
import com.kz.redminesweeper.view.FilterListItem_;

import java.util.List;

public class FilterListAdapter extends ArrayAdapter<IssuesFilter> {

    LayoutInflater layoutInflater;

    public FilterListAdapter(Context context, int layoutId, List<IssuesFilter> list)  {
        super(context, layoutId, list);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = FilterListItem_.build(getContext());
        }
        ((FilterListItem)convertView).bind(getItem(position));
        return convertView;
    }
}
