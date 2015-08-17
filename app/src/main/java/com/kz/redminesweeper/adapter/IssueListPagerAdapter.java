package com.kz.redminesweeper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.kz.redminesweeper.bean.Project;
import com.kz.redminesweeper.fragment.IssueListFragment;

import java.util.List;

public class IssueListPagerAdapter extends FragmentStatePagerAdapter {

    private ViewPager pager;

    private List<Project> projects;

    public IssueListPagerAdapter(FragmentManager fm, ViewPager pager, List<Project> projects) {
        super(fm);
        this.projects = projects;
        this.pager = pager;
        pager.setOffscreenPageLimit(4);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName() + ":" + position);
        return IssueListFragment.newInstance(projects.get(position));
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return projects.get(position).getName();
    }

    public void updateIssueList() {
        Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName());
        for (int i = 0; i < getCount(); i++) {
            try {
                IssueListFragment fragment = (IssueListFragment)instantiateItem(pager, i);
                if (fragment == null) throw new Exception();
                fragment.clearList();
            } catch (Exception e) {
                Log.e(getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), e);
            }
        }
    }
}
