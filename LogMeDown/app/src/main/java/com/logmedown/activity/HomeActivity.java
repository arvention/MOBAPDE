package com.logmedown.activity;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.logmedown.adapter.PagerAdapter;
import com.example.arces.logmedown.R;
import com.logmedown.fragment.BlocFragment;
import com.logmedown.fragment.HomeFragment;
import com.logmedown.fragment.ProfileFragment;
import com.logmedown.model.Note;
import com.logmedown.model.User;

public class HomeActivity extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    public SharedPreferences sharedPreferences;
    public static User loggedUser;

    private TextView fragmentName;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TypedArray tabNames;

    private HomeFragment homeFragment;
    private BlocFragment blocFragment;
    private ProfileFragment profileFragment;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageButton appLogo;

    private Animation zoomIn;
    private Animation zoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);

        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        loggedUser = (User) getIntent().getSerializableExtra("logged_user");

        Log.d("logged_user", "Name: " + loggedUser.getFirstName() + " " + loggedUser.getLastName() + "" +
                " Username: " + loggedUser.getUsername() + " Email Address: " + loggedUser.getEmailAddress());

        fragmentName = (TextView) findViewById(R.id.fragmentName);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabNames = getResources().obtainTypedArray(R.array.tabNames);
        for (int i = 0; i < tabNames.length(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabNames.getText(i)));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(2);

        homeFragment = (HomeFragment) pagerAdapter.getItem(0);
        blocFragment = (BlocFragment) pagerAdapter.getItem(1);
        profileFragment = (ProfileFragment) pagerAdapter.getItem(2);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);

                drawerLayout.closeDrawers();
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int index = -1;
                for (int i = 0; i < tabNames.length() && index == -1; i++) {
                    if (item.getTitle().toString().equalsIgnoreCase(tabLayout.getTabAt(i).getText().toString())) {
                        index = i;
                    }
                }
                if (index != -1) {
                    viewPager.setCurrentItem(index);
                    fragmentName.setText(item.getTitle().toString());
                } else {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Log Out")
                            .setMessage(R.string.textLogOutPrompt)
                            .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    finish();
                                }

                            })
                            .setNegativeButton(R.string.textLogOutNo, null)
                            .show();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        appLogo = (ImageButton) findViewById(R.id.app_logo);
        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                fragmentName.setText(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText());
                if (tab.getPosition() == 0) {
                    FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                    fm.detach(homeFragment).attach(homeFragment).commit();
                    if(getSupportFragmentManager().executePendingTransactions())
                        homeFragment.getHomeAddNoteFab().startAnimation(zoomOut);
                } else if (tab.getPosition() == 1) {
                    //bloc.getBlocAddNoteFab().startAnimation(zoomOut);
                } else if (tab.getPosition() == 2) {
                    FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                    fm.detach(profileFragment).attach(profileFragment).commit();
                    if(getSupportFragmentManager().executePendingTransactions())
                        profileFragment.getProfileAddNoteFab().startAnimation(zoomOut);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    homeFragment.getHomeAddNoteFab().startAnimation(zoomIn);
                } else if (tab.getPosition() == 1) {
                    //bloc.getBlocAddNoteFab().startAnimation(zoomIn);
                } else if (tab.getPosition() == 2) {
                    profileFragment.getProfileAddNoteFab().startAnimation(zoomIn);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultActivity.class)));
        return true;
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Note editedNote;

        switch(resultCode){
            case 1:
                Log.d("result_test", "SUCCESS");
                editedNote = (Note) data.getSerializableExtra("edited_note");

                int position = data.getIntExtra("position", 0);

                loggedUser.getNoteAt(position).setContent(editedNote.getContent());
                loggedUser.getNoteAt(position).setTitle(editedNote.getTitle());

                homeFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                profileFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                break;
            case 2:
                Log.d("result_home_test", "SUCCESS");
                editedNote = (Note) data.getSerializableExtra("saved_note");

                loggedUser.addNote(editedNote);
                homeFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                profileFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                break;
            default:
                Log.d("result_test_home", "FAIL");
                break;
        }
    }
}
