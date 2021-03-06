package com.kz.redminesweeper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.header_account)
public class AccountHeader extends LinearLayout {

    @ViewById
    TextView userNameLabel;

    @ViewById
    TextView rootUrlLabel;

    @ViewById
    ImageView modeChangeButton;


    public AccountHeader(Context context) {
        super(context);
    }

    public AccountHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bind(Account account) {
        User user = account.getUser();
        userNameLabel.setText(getContext().getString(R.string.user_name_label, user.getFirstname(), user.getLastname()));
        rootUrlLabel.setText(account.getRootUrl());
    }

    public void changeNavigationMode() {
        modeChangeButton.setActivated(!modeChangeButton.isActivated());
    }

}