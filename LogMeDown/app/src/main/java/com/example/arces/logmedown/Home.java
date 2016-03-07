package com.example.arces.logmedown;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class Home extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    public SharedPreferences sharedPreferences;

    private Button buttonLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView fragmentName = (TextView) findViewById(R.id.fragmentName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final TypedArray arrayTabNames = getResources().obtainTypedArray(R.array.tabNames);
        for(int  i = 0; i < arrayTabNames.length(); i++){
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


        final String[] arrayNavigationNames = getResources().getStringArray(R.array.navigationNames);
        final DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, arrayNavigationNames));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = -1;
                for(int i = 0; i < arrayTabNames.length(); i++){
                    if(tabLayout.getTabAt(i).getText().toString().equalsIgnoreCase(arrayNavigationNames[position])){
                        index = i;
                    }
                }
                if(index < arrayTabNames.length() && index  != -1) {
                    viewPager.setCurrentItem(index);
                    fragmentName.setText(tabLayout.getTabAt(index).getText());
                    drawer.closeDrawer(drawerList);
                }
            }
        });

        final ImageButton appLogo = (ImageButton)findViewById(R.id.app_logo);
        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(drawerList);
            }
        });


        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("loggedUser", "");
        User user = gson.fromJson(json, User.class);

        Log.d("logged_user", "Name: " + user.getFirstName() + " " + user.getLastName() + "" +
                " Username: " + user.getUsername() + " Email Address: " + user.getEmailAddress());
        /*buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });*/
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
