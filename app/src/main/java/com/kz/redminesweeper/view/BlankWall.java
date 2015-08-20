package com.kz.redminesweeper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
    TextView freeLabel;

    @ViewById
    TextView freeSubLabel;

    ViewGroup parent;

    boolean clickHide;

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
        freeLabel.setText(getContext().getString(id));
    }

    public void setSubTitle(int id) {
        freeSubLabel.setVisibility(VISIBLE);
        freeSubLabel.setText(getContext().getString(id));
    }

    public void show(ViewGroup view) {
        parent = view;
        parent.addView(this, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
