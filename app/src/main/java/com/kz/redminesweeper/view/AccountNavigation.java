package com.kz.redminesweeper.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.bean.Account;
import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.account_nagivation)
public class AccountNavigation extends LinearLayout {

    @ViewById
    TextView userNameLabel;

    @ViewById
    TextView rootUrlLabel;


    public AccountNavigation(Context context) {
        super(context);
    }

    public void bind(Account account) {
        User user = account.getUser();
        userNameLabel.setText(getContext().getString(R.string.user_name_label, user.getFirstname(), user.getLastname()));
        rootUrlLabel.setText(account.getRootUrl());

    }

}