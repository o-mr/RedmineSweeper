package com.kz.redminesweeper.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.kz.redminesweeper.AccountSettingsActivity;
import com.kz.redminesweeper.MainActivity;
import com.kz.redminesweeper.R;
import com.kz.redminesweeper.account.Account;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup(R.layout.footer_account)
public class AccountFooter extends LinearLayout {

    public AccountFooter(Context context) {
        super(context);
    }

    @Click(R.id.base_layout)
    public void addNewAccount() {
        ((MainActivity)getContext()).startAccountSettings(new Account(), AccountSettingsActivity.Mode.ADD, 0);
    }

}