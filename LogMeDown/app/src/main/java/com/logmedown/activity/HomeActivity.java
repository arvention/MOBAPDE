package com.logmedown.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.adapter.PagerAdapter;
import com.logmedown.database.Database;
import com.logmedown.fragment.BlocFragment;
import com.logmedown.fragment.HomeFragment;
import com.logmedown.fragment.ProfileFragment;
import com.logmedown.model.Bloc;
import com.logmedown.model.Note;
import com.logmedown.model.User;

public class HomeActivity extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    public SharedPreferences sharedPreferences;
    public User loggedUser;
    public Database db;

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

    private MenuItem menuItem;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);

        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        loggedUser = (User) getIntent().getSerializableExtra("logged_user");
        db = Database.getInstance(this);

        Log.d("logged_user", "Name: " + loggedUser.getFirstName() + " " + loggedUser.getLastName() + "" +
                " Username: " + loggedUser.getUsername() + " Email Address: " + loggedUser.getEmailAddress());

        fragmentName = (TextView) findViewById(R.id.fragment_name);

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
                    loggedUser = db.getUser(loggedUser.getUserID());
                    fm.detach(homeFragment).attach(homeFragment).commit();
                    if(getSupportFragmentManager().executePendingTransactions()) {
                        homeFragment.getHomeAddNoteFab().startAnimation(zoomOut);
                        homeFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                    }
                } else if (tab.getPosition() == 1) {
                    FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                    loggedUser = db.getUser(loggedUser.getUserID());
                    fm.detach(blocFragment).attach(blocFragment).commit();
                    if(getSupportFragmentManager().executePendingTransactions()) {
                        blocFragment.getBlocAddBlocFab().startAnimation(zoomOut);
                        blocFragment.getBlocRecyclerAdapter().notifyDataSetChanged();
                    }
                } else if (tab.getPosition() == 2) {
                    FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                    loggedUser = db.getUser(loggedUser.getUserID());
                    fm.detach(profileFragment).attach(profileFragment).commit();
                    if(getSupportFragmentManager().executePendingTransactions()) {
                        profileFragment.getProfileAddNoteFab().startAnimation(zoomOut);
                        profileFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    homeFragment.getHomeAddNoteFab().startAnimation(zoomIn);
                } else if (tab.getPosition() == 1) {
                    blocFragment.getBlocAddBlocFab().startAnimation(zoomIn);
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

        menuItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultActivity.class)));
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        searchView.setQuery("", false);
        menuItem.collapseActionView();
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

        switch(resultCode){
            case 1:
                Note editedNote = (Note) data.getSerializableExtra("edited_note");

                int position = data.getIntExtra("position", 0);

                loggedUser.getNoteAt(position).setContent(editedNote.getContent());
                loggedUser.getNoteAt(position).setTitle(editedNote.getTitle());

                homeFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                profileFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                break;
            case 2:
                Note savedNote = (Note) data.getSerializableExtra("saved_note");

                loggedUser.addNote(savedNote);
                homeFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                profileFragment.getNoteRecyclerAdapter().notifyDataSetChanged();
                break;
            case 3:
                Bloc bloc = (Bloc)data.getSerializableExtra("saved_bloc");

                loggedUser.getBlocs().add(bloc);
                blocFragment.getBlocRecyclerAdapter().notifyDataSetChanged();
                break;
            case 4:
                String action = (String)data.getSerializableExtra("action");
                if(action.equals("delete")){
                    int positionToDelete = (Integer)data.getSerializableExtra("position");
                    loggedUser.getBlocs().remove(positionToDelete);
                    blocFragment.getBlocRecyclerAdapter().notifyDataSetChanged();
                }
                break;
            default:
                Log.d("result_test_home", "FAIL");
                break;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        // check if search intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("logged_user", loggedUser);
        }

        super.startActivity(intent);
    }
}
