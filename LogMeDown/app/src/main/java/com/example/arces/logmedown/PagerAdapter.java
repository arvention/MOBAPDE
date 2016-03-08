package com.example.arces.logmedown;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.arces.logmedown.BlocFragment;
import com.example.arces.logmedown.HomeFragment;
import com.example.arces.logmedown.ProfileFragment;

public class PagerAdapter extends FragmentStatePagerAdapter{
    int numTabs;

    public PagerAdapter(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                HomeFragment homeTab = new HomeFragment();
                return homeTab;
            case 1:
                BlocFragment blocTab = new BlocFragment();
                return blocTab;
            case 2:
                ProfileFragment profileTab = new ProfileFragment();
                return profileTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
