package com.kz.redminesweeper.view;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.R;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.wall_blank)
public class BlankWall extends LinearLayout {

    @ViewById
    LinearLayout baseLayout;

    @ViewById
    TextView freeLabel;

    @ViewById
    TextView freeSubLabel;

    ViewGroup parent;

    boolean clickHide;

    boolean hideActionBar;

    public BlankWall(Context context) {
        super(context);
    }

    public BlankWall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlankWall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTitle(int id) {
        setTitle(id, 32);
    }

    public void setTitle(int id, float size) {
        freeLabel.setText(getContext().getString(id));
        freeLabel.setTextSize(size);
    }

    public void setSubTitle(int id) {
        setSubTitle(id, 16);
    }

    public void setSubTitle(int id, float size) {
        freeSubLabel.setVisibility(VISIBLE);
        freeSubLabel.setText(getContext().getString(id));
        freeSubLabel.setTextSize(size);
    }

    public void setWallColor(int colorId) {
        baseLayout.setBackgroundColor(getContext().getResources().getColor(colorId));
    }

    public void show(ViewGroup view) {
        parent = view;
        parent.addView(this, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
        if (hideActionBar && actionBar != null) actionBar.hide();
    }

    public void setHideActionBar(boolean hideActionBar) {
        this.hideActionBar = hideActionBar;
    }

    public void setClickHide(boolean clickHide) {
        this.clickHide = clickHide;
    }

    @Click({R.id.free_label, R.id.free_sub_label})
    public void clickHide() {
        if(clickHide) hide();
    }

    @UiThread
    public void hide() {
        if (parent != null) {
            parent.removeView(this);
            parent = null;
        }
        ActionBar actionBar = ((AppCompatActivity)getContext()).getSupportActionBar();
        if (hideActionBar && actionBar != null) actionBar.show();
    }

    public void setTimer(int timeout_ms) {
        startTimer(timeout_ms);
    }

    @Background
    void startTimer(int timeout_ms) {
        try {
            Thread.sleep(timeout_ms);
            hide();
        } catch (Exception e) {

        }
    }

}
