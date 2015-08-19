package com.kz.redminesweeper.view;

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.kz.redminesweeper.R;

public class NavigationToggle extends ActionBarDrawerToggle {

    private FragmentActivity mActivity;

    public NavigationToggle(FragmentActivity activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActivity = activity;
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                syncState();
            }
        });
        drawerLayout.setDrawerListener(this);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        try {
            mActivity.supportInvalidateOptionsMenu();
        }catch (Exception e) {
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        try {
            mActivity.supportInvalidateOptionsMenu();
        }catch (Exception e) {
        }
    }

}
