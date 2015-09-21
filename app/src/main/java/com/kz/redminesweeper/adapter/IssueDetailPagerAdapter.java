package com.kz.redminesweeper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.kz.redminesweeper.ActivityErrorReceiver;
import com.kz.redminesweeper.bean.Issues;
import com.kz.redminesweeper.fragment.IssueDetailFragment;

public class IssueDetailPagerAdapter extends FragmentStatePagerAdapter {

    private ViewPager pager;

    private Issues issues;

    private ActivityErrorReceiver errorReceiver;

    private int OFF_SCREEN_PAGE_LIMIT = 1;

    public IssueDetailPagerAdapter(FragmentManager fm, ViewPager pager, Issues issues, ActivityErrorReceiver errorReceiver) {
        super(fm);
        this.issues = issues;
        this.pager = pager;
        this.errorReceiver = errorReceiver;
        pager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
    }

    public void setCurrentItem(int position) {
        pager.setCurrentItem(position);
    }

    @Override
    public Fragment getItem(int position) {
        IssueDetailFragment fragment = IssueDetailFragment.newInstance(issues.getIssues().get(position));
        fragment.setErrorReceiver(errorReceiver);
        return fragment;
    }

    @Override
    public int getCount() {
        return issues.getIssues().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(issues.getIssues().get(position).getId());
    }

    public interface IssueDetailPagerAdapterCallbacks {
        void onAddPage();
    }
}
