package com.kz.redminesweeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.view.AccountListItem;
import com.kz.redminesweeper.view.AccountListItem_;
import com.kz.redminesweeper.view.FilterListItem_;

import java.util.List;

public class AccountListAdapter extends ArrayAdapter<Account> {

    LayoutInflater layoutInflater;

    public AccountListAdapter(Context context, int layoutId, int viewId, List<Account> list)  {
        super(context, layoutId, viewId, list);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = AccountListItem_.build(getContext());
        }
        ((AccountListItem)convertView).bind(getItem(position));
        return convertView;
    }
}
