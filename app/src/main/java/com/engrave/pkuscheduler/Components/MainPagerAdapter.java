package com.engrave.pkuscheduler.Components;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.engrave.pkuscheduler.Fragments.CourseListFragment;
import com.engrave.pkuscheduler.Fragments.ScheduleListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments = new ArrayList<Fragment>(){
        {
            add(new ScheduleListFragment());
            add(new CourseListFragment());
        }
    };
    private static final int NUM_PAGES = 2;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }


}
