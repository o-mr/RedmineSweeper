package com.kz.redminesweeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kz.redminesweeper.bean.Issue;
import com.kz.redminesweeper.view.IssueListItem;
import com.kz.redminesweeper.view.IssueListItem_;

import java.util.List;

public class IssueListAdapter extends ArrayAdapter<Issue> {

    LayoutInflater layoutInflater;

    private List<Issue> list;

    public IssueListAdapter(Context context, int layoutId, int viewId, List<Issue> list)  {
        super(context, layoutId, viewId, list);
        this.list = list;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = IssueListItem_.build(getContext());
        }
        ((IssueListItem)convertView).bind(getItem(position));
        return convertView;
    }

    public List<Issue> getList() {
        return list;
    }

}
