package com.logmedown.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.logmedown.fragment.BlocFragment;
import com.logmedown.fragment.HomeFragment;
import com.logmedown.fragment.ProfileFragment;

public class PagerAdapter extends FragmentStatePagerAdapter{

    int numTabs;
    private HomeFragment homeTab;
    private BlocFragment blocTab;
    private ProfileFragment profileTab;

    public PagerAdapter(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;

        homeTab = new HomeFragment();
        blocTab = new BlocFragment();
        profileTab = new ProfileFragment();

    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return homeTab;
            case 1: return blocTab;
            case 2: return profileTab;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
