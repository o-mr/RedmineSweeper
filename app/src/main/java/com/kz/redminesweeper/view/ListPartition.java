package com.kz.redminesweeper.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.adapter.FilterListAdapter;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Partition;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_partition)
public class ListPartition extends LinearLayout {

    @ViewById
    TextView titleLabel;

    public ListPartition(Context context) {
        super(context);
    }

    public ListPartition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListPartition(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bind(Partition partition) {
        titleLabel.setText(partition.getLabel());
    }
}
