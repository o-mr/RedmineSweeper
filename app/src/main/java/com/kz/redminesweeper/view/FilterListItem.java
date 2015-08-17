package com.kz.redminesweeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Status;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.filter_list_item)
public class FilterListItem extends LinearLayout {

    @ViewById
    TextView idLabel;

    @ViewById
    TextView nameLabel;


    public FilterListItem(Context context) {
        super(context);
    }

    public void bind(IssuesFilter filter) {
        GradientDrawable background = (GradientDrawable)idLabel.getBackground();
        background.setColor(getContext().getResources().getColor(filter.getColorId()));
        idLabel.setText(filter.getLabel());
        nameLabel.setText(filter.getName());
    }

}
