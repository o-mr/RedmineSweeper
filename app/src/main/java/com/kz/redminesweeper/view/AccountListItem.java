package com.kz.redminesweeper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.AccountSettingsActivity;
import com.kz.redminesweeper.MainActivity;
import com.kz.redminesweeper.R;
import com.kz.redminesweeper.account.Account;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_account)
public class AccountListItem extends LinearLayout {

    @ViewById
    TextView idLabel;

    @ViewById
    TextView loginIdLabel;

    @ViewById
    TextView urlLabel;

    @ViewById
    ImageButton editButton;

    private Account account;


    public AccountListItem(Context context) {
        super(context);
    }

    public AccountListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bind(Account account) {
        idLabel.setText(account.getLoginId().substring(0, 1).toUpperCase());
        loginIdLabel.setText(account.getLoginId());
        urlLabel.setText(account.getRootUrl());
        this.account = account;
    }

    @Click(R.id.edit_button)
    public void editAccount() {
        ((MainActivity)getContext()).startAccountSettings(account, AccountSettingsActivity.Mode.EDIT);
    }

}
