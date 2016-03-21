package com.logmedown.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.logmedown.fragment.BlocFragment;
import com.logmedown.fragment.HomeFragment;
import com.logmedown.fragment.ProfileFragment;
import com.logmedown.fragment.SearchBlocFragment;
import com.logmedown.fragment.SearchNoteFragment;
import com.logmedown.fragment.SearchUserFragment;

public class SearchPagerAdapter extends FragmentStatePagerAdapter{

    int numTabs;
    private SearchNoteFragment searchNoteTab;
    private SearchBlocFragment searchBlocTab;
    private SearchUserFragment searchUserTab;

    public SearchPagerAdapter(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;

        searchNoteTab = new SearchNoteFragment();
        searchBlocTab = new SearchBlocFragment();
        searchUserTab = new SearchUserFragment();


    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return searchNoteTab;
            case 1: return searchBlocTab;
            case 2: return searchUserTab;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
