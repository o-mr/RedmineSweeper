package com.kz.redminesweeper.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.bean.IssuesFilter;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_filter)
public class FilterListItem extends LinearLayout {

    @App
    RmSApplication app;

    @ViewById
    TextView idLabel;

    @ViewById
    TextView nameLabel;

    private IssuesFilter filter;

    public FilterListItem(Context context) {
        super(context);
    }

    public FilterListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bind(IssuesFilter filter) {
        GradientDrawable background = (GradientDrawable)idLabel.getBackground();
        background.setColor(getContext().getResources().getColor(filter.getColorId()));
        idLabel.setText(filter.getLabel());
        nameLabel.setText(filter.getName());
        this.filter = filter;
    }


}
