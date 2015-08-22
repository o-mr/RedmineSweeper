package com.kz.redminesweeper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;

import com.kz.redminesweeper.ActivityErrorReceiver;
import com.kz.redminesweeper.bean.IssuesFilter;
import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.fragment.IssueListFragment;

import java.util.List;

public class IssueListPagerAdapter extends FragmentStatePagerAdapter {

    private ViewPager pager;

    private List<Project> projects;

    private ActivityErrorReceiver errorReceiver;

    public IssueListPagerAdapter(FragmentManager fm, ViewPager pager, List<Project> projects, ActivityErrorReceiver errorReceiver) {
        super(fm);
        this.projects = projects;
        this.pager = pager;
        this.errorReceiver = errorReceiver;
        pager.setOffscreenPageLimit(4);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName() + ":" + position);
        IssueListFragment fragment = IssueListFragment.newInstance(projects.get(position));
        fragment.setErrorReceiver(errorReceiver);
        return fragment;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return projects.get(position).getName();
    }

    public void updateIssueList(IssuesFilter filter) {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        for (int i = 0; i < getCount(); i++) {
            try {
                IssueListFragment fragment = (IssueListFragment)instantiateItem(pager, i);
                fragment.onChangeFilter(filter);
            } catch (Exception e) {
                Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
            }
        }
    }
}
