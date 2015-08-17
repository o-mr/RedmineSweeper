package com.kz.redminesweeper.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateFormat;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.bean.Issue;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.issue_list_item)
public class IssueListItem extends LinearLayout {

    @ViewById
    TextView trackerLabel;

    @ViewById
    TextView priorityLabel;

    @ViewById
    TextView idLabel;

    @ViewById
    TextView subjectLabel;

    @ViewById
    TextView descriptionLabel;

    @ViewById
    TextView createdInfoLabel;

    public IssueListItem(Context context) {
        super(context);
    }

    public void bind(Issue issue) {
        GradientDrawable background = (GradientDrawable)trackerLabel.getBackground();
        background.setColor(getContext().getResources().getColor(issue.getTracker().getColorId()));
        trackerLabel.setText(issue.getTracker().getName());
        priorityLabel.setText(issue.getPriority().getName());
        idLabel.setText("#" + issue.getId());
        subjectLabel.setText(issue.getSubject());
        descriptionLabel.setText(issue.getDescription());
        String createdDate = DateFormat.format(getContext().getString(R.string.date_format), issue.getCreated_on()).toString();
        createdInfoLabel.setText(getContext().getString(R.string.created_info_label, issue.getAuthor().getName(), createdDate));
    }

}
