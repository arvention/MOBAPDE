package com.logmedown.activity;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.logmedown.adapter.PagerAdapter;
import com.example.arces.logmedown.R;
import com.logmedown.fragment.ProfileFragment;
import com.logmedown.model.Note;
import com.logmedown.model.User;

public class HomeActivity extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    public SharedPreferences sharedPreferences;
    private User loggedUser;

    private TextView fragmentName;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ProfileFragment profileFragment;
    private TypedArray tabNames;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageButton appLogo;
    private FloatingActionButton addNoteFab;

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
        profileFragment = (ProfileFragment) pagerAdapter.getItem(2);
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

        appLogo = (ImageButton) findViewById(R.id.app_logo);
        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        /*addNoteFab = (FloatingActionButton) findViewById(R.id.addNoteFab);

        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNoteFab.startAnimation(zoomIn);

                Intent intent = new Intent(HomeActivity.this, NoteActivity.class);
                intent.putExtra("logged_user", loggedUser);
                intent.putExtra("note_action", "add");
                startActivityForResult(intent, 2);
            }
        });*/


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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //addNoteFab.startAnimation(zoomOut);

        switch(resultCode){
            case 2:
                Log.d("result_home_test", "SUCCESS");
                Note editedNote = (Note) data.getSerializableExtra("saved_note");

                Log.d("result_home_test", editedNote.getTitle());
                //loggedUser.addNote(editedNote);
                profileFragment.addNoteToUser(editedNote);
                break;
            default:
                Log.d("result_test_home", "FAIL");
                break;
        }
    }
}
