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
import com.logmedown.fragment.SearchBlocFragment;
import com.logmedown.fragment.SearchNoteFragment;
import com.logmedown.fragment.SearchUserFragment;
import com.logmedown.model.Bloc;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private Database db;
    private TextView searchName;
    private Toolbar toolbar;

    private TabLayout searchTabLayout;
    private TypedArray tabNames;
    private ViewPager viewPager;
    private SearchPagerAdapter searchPagerAdapter;

    private SearchNoteFragment noteFragment;
    private SearchUserFragment userFragment;
    private SearchBlocFragment blocFragment;

    private SearchView searchView;
    private MenuItem menuItem;

    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        //database
        db = Database.getInstance(this);

        //logged user
        loggedUser = (User) getIntent().getSerializableExtra("logged_user");

        //components
        searchName = (TextView) findViewById(R.id.searchName);
        toolbar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

        noteFragment = (SearchNoteFragment) searchPagerAdapter.getItem(0);
        blocFragment = (SearchBlocFragment) searchPagerAdapter.getItem(1);
        userFragment = (SearchUserFragment) searchPagerAdapter.getItem(2);

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

        //handle query
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

            ArrayList<Note> noteList = db.searchNotes(query);
            noteFragment.updateList(noteList);

            ArrayList<User> userList = db.searchUsers(query);
            userFragment.updateList(userList);

            ArrayList<Bloc> blocList = db.searchBlocs(query);
            blocFragment.updateList(blocList);

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

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}