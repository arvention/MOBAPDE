package com.logmedown.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arces.logmedown.R;
import com.logmedown.adapter.PagerAdapter;
import com.logmedown.adapter.SearchPagerAdapter;
import com.logmedown.database.Database;

public class SearchResultActivity extends AppCompatActivity {
    private Database db;
    private TextView searchName;
    private Toolbar toolbar;

    private TabLayout searchTabLayout;
    private TypedArray tabNames;
    private ViewPager viewPager;
    private SearchPagerAdapter searchPagerAdapter;

    private SearchView searchView;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        //database
        db = Database.getInstance(this);

        //components
        searchName = (TextView) findViewById(R.id.searchName);
        toolbar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchTabLayout = (TabLayout) findViewById(R.id.search_tab);

        tabNames = getResources().obtainTypedArray(R.array.searchTabNames);
        for (int i = 0; i < tabNames.length(); i++) {
            searchTabLayout.addTab(searchTabLayout.newTab().setText(tabNames.getText(i)));
        }

        searchTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.search_pager);
        searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), searchTabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(searchPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(searchTabLayout));

        searchTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //get query
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchName.setText("Search results for " + query + ":");

            if(searchView != null) {
                searchView.setQuery("", false);
                menuItem.collapseActionView();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        menuItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultActivity.class)));
        return true;
    }
}