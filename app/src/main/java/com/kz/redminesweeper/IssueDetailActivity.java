package com.kz.redminesweeper;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kz.redminesweeper.bean.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_issue_detail)
public class IssueDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1002;

    @App
    RmSApplication app;

    @ViewById
    LinearLayout baseLayout;

    @ViewById
    LinearLayout titleLabel;

    @ViewById
    TextView idLabel;

    @ViewById
    TextView subjectLabel;

    @ViewById
    TextView projectNameLabel;

    @ViewById
    TextView createdInfoLabel;

    @ViewById
    TextView assignedToLabel;

    @ViewById
    TextView startDateLabel;

    @ViewById
    TextView priorityLabel;

    @ViewById
    TextView statusLabel;

    @ViewById
    ProgressBar doneRatioBar;

    @ViewById
    TextView doneRatioLabel;

    @ViewById
    TextView descriptionLabel;

    @Extra
    Issue issue;

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        bind();
        createActionBar();
    }

    void bind() {

        Drawable drawable = getDrawableById(issue.getTracker().getColorId());
        titleLabel.setBackground(drawable);

        idLabel.setText(String.valueOf(issue.getId()));
        subjectLabel.setText(issue.getSubject());
        projectNameLabel.setText(issue.getProject().getName());
        String createdDate = DateFormat.format(getString(R.string.date_format), issue.getCreated_on()).toString();
        createdInfoLabel.setText(getString(R.string.created_info_label, issue.getAuthor().getName(), createdDate));

        assignedToLabel.setText(issue.getAssigned_to().getName());
        String startDate = DateFormat.format(getString(R.string.date_format), issue.getStart_date()).toString();
        startDateLabel.setText(startDate);
        priorityLabel.setText(issue.getPriority().getName());
        statusLabel.setText(issue.getStatus().getName());
        doneRatioBar.setProgress(issue.getDone_ratio());
        doneRatioLabel.setText(issue.getDone_ratio() + "%");

        descriptionLabel.setText(issue.getDescription());
        //descriptionLabel.setMovementMethod();
    }

    void createActionBar() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);
        actionBar.show();
        actionBar.setTitle(issue.getTracker().getName());
        Drawable drawable = getDrawableById(issue.getTracker().getColorId());
        actionBar.setBackgroundDrawable(drawable);
    }

    @SuppressWarnings("deprecation")
    public Drawable getDrawableById(int id) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }

}
