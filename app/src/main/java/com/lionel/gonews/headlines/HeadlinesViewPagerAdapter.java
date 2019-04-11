package com.lionel.gonews.headlines;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class HeadlinesViewPagerAdapter extends FragmentPagerAdapter {

    private final List<String> topics;
    private final List<Fragment> fragments;

    public HeadlinesViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> topics) {
        super(fm);

        this.fragments = fragments;
        this.topics = topics;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return topics != null ? topics.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return topics.get(position);
    }
}
