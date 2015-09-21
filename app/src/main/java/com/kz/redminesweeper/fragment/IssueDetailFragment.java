package com.kz.redminesweeper.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kz.redminesweeper.ActivityErrorReceiver;
import com.kz.redminesweeper.R;
import com.kz.redminesweeper.RmSApplication;
import com.kz.redminesweeper.bean.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_issue_detail)
public class IssueDetailFragment extends Fragment {

    @App
    RmSApplication app;

    @ViewById
    LinearLayout baseLayout;

    @ViewById
    LinearLayout titleLabel;

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

    @FragmentArg
    Issue issue;

    private ActivityErrorReceiver errorReceiver;

    public static IssueDetailFragment newInstance(Issue issue) {
        return IssueDetailFragment_.builder().issue(issue).build();
    }

    @AfterViews
    void setUp() {
        Log.v(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());

        Drawable drawable = getDrawableById(issue.getTracker().getColorId());
        titleLabel.setBackground(drawable);

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

    public void setErrorReceiver(ActivityErrorReceiver errorReceiver) {
        this.errorReceiver = errorReceiver;
    }

    @SuppressWarnings("deprecation")
    public Drawable getDrawableById(int id) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getActivity().getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }
}
