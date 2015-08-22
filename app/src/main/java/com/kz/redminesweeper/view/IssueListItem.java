package com.kz.redminesweeper.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kz.redminesweeper.R;
import com.kz.redminesweeper.bean.Issue;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_issue)
public class IssueListItem extends LinearLayout {

    @ViewById
    TextView trackerLabel;

    @ViewById
    ImageView trackerCheck;

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

    private Issue issue;

    public IssueListItem(Context context) {
        super(context);
    }

    public IssueListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IssueListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        this.issue = issue;
    }

    @Click({R.id.tracker_label, R.id.tracker_check})
    public void editIssues() {
        if (trackerLabel.getVisibility() == VISIBLE) {
            trackerLabel.setVisibility(GONE);
            trackerCheck.setVisibility(VISIBLE);
        } else {
            trackerLabel.setVisibility(VISIBLE);
            trackerCheck.setVisibility(GONE);
        }
        Toast.makeText(getContext(), issue.getTracker().getName(), Toast.LENGTH_SHORT).show();
    }

}
