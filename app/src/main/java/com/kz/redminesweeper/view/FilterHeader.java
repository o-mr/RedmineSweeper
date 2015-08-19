package com.kz.redminesweeper.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.account.Account;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.User;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.header_filter)
public class FilterHeader extends LinearLayout {

    @ViewById
    LinearLayout baseLayout;

    @ViewById
    TextView idLabel;

    @ViewById
    TextView nameLabel;


    public FilterHeader(Context context) {
        super(context);
    }

    public FilterHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bind(IssuesFilter filter) {
        int color = getContext().getResources().getColor(filter.getColorId());
        baseLayout.setBackgroundColor(color);
        GradientDrawable background = (GradientDrawable)idLabel.getBackground();
        background.setColor(getContext().getResources().getColor(R.color.bg_standard));
        idLabel.setTextColor(color);
        idLabel.setText(filter.getLabel());
        nameLabel.setText(filter.getName());
    }

}