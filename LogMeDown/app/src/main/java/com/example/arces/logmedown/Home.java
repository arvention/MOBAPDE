package com.example.arces.logmedown;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

public class Home extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    public SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);

        loggedUser = (User) getIntent().getSerializableExtra("logged_user");

        Log.d("logged_user", "Name: " + loggedUser.getFirstName() + " " + loggedUser.getLastName() + "" +
                " Username: " + loggedUser.getUsername() + " Email Address: " + loggedUser.getEmailAddress());

        final TextView fragmentName = (TextView) findViewById(R.id.fragmentName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final TypedArray arrayTabNames = getResources().obtainTypedArray(R.array.tabNames);
        for (int i = 0; i < arrayTabNames.length(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(arrayTabNames.getText(i)));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                fragmentName.setText(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


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
                for (int i = 0; i < arrayTabNames.length() && index == -1; i++) {
                    if (item.getTitle().toString().equalsIgnoreCase(tabLayout.getTabAt(i).getText().toString())) {
                        index = i;
                    }
                }
                if (index != -1) {
                    viewPager.setCurrentItem(index);
                    fragmentName.setText(item.getTitle().toString());
                } else {
                    new AlertDialog.Builder(Home.this)
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

        //final String[] arrayNavigationNames = getResources().getStringArray(R.array.navigationNames);
        //final int[] arrayNavigationImages = {0,0,0,0};
        //final DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        //final ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        //View drawerHeader = getLayoutInflater().inflate(R.layout.drawer_header, null, false);
        //TextView headerName = (TextView)findViewById(R.id.header_profile_name);
        //headerName.setText(user.getFirstName() + " " + user.getLastName());
        //TextView headerEmail = (TextView)findViewById(R.id.header_profile_email);
        //headerEmail.setText(user.getEmailAddress());
        //drawerList.addHeaderView(drawerHeader);

        //drawerList.setAdapter(new DrawerListItemAdapter(this, arrayNavigationNames, arrayNavigationImages,viewPager,fragmentName, drawer, drawerList, tabLayout, arrayTabNames.length()));

        final ImageButton appLogo = (ImageButton) findViewById(R.id.app_logo);
        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, NoteActivity.class);
                intent.putExtra("logged_user", loggedUser);
                intent.putExtra("note_action", "add");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
