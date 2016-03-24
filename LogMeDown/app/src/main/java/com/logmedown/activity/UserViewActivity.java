package com.logmedown.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arces.logmedown.R;
import com.logmedown.adapter.UserViewNoteRecyclerAdapter;
import com.logmedown.database.Database;
import com.logmedown.model.User;

public class UserViewActivity extends AppCompatActivity {
    private TextView username, toolbarName, friendText;
    private User user, loggedUser;
    private Button addFriendButton, removeFriendButton, cancelRequestButton, acceptRequestButton, declineRequestButton;

    private RelativeLayout friendContainer;
    private LinearLayout requestContainer;

    private RecyclerView recyclerView;
    private UserViewNoteRecyclerAdapter uvnra;

    private Toolbar toolbar;
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        //retrieve user data and logged user data
        user = (User) getIntent().getSerializableExtra("view_user");
        loggedUser = (User) getIntent().getSerializableExtra("logged_user");

        //Database
        db = Database.getInstance(this);

        //toolbar components
        toolbar = (Toolbar) findViewById(R.id.user_view_toolbar);
        setSupportActionBar(toolbar);
        toolbarName = (TextView) findViewById(R.id.user_view_toolbar_name);
        toolbarName.setText(user.getFirstName() + " " + user.getLastName());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //body components
        username = (TextView) findViewById(R.id.user_view_user_name);
        username.setText(user.getFirstName() + " " + user.getLastName());
        friendContainer = (RelativeLayout) findViewById(R.id.user_view_friend_container);
        requestContainer = (LinearLayout) findViewById(R.id.user_view_respond_request_container);

        addFriendButton = (Button) findViewById(R.id.user_view_add_friend);
        removeFriendButton = (Button) findViewById(R.id.user_view_remove_friend);
        cancelRequestButton = (Button) findViewById(R.id.user_view_cancel_request);
        acceptRequestButton = (Button) findViewById(R.id.user_view_accept_request);
        declineRequestButton = (Button) findViewById(R.id.user_view_decline_request);

        friendText = (TextView) findViewById(R.id.user_view_friend_text);

        Log.d("view_user_debug", user.getUserID() + " vs " + loggedUser.getUserID());
        if(user.getUserID() == loggedUser.getUserID()){
            friendContainer.setVisibility(View.GONE);
            addFriendButton.setVisibility(View.GONE);
        }
        else if (db.isFriend(loggedUser, user) || db.hasFriendRequest(loggedUser, user) || db.hasFriendRequest(user, loggedUser)) {
            if (db.isFriend(loggedUser, user)) {
                changeViewToRemove();
            } else if (db.hasFriendRequest(loggedUser, user)) {
                changeViewToCancelRequest();
            } else if (db.hasFriendRequest(user, loggedUser)) {
                changeViewToRespondRequest();
            }
        } else {
            changeViewToAdd();
        }
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFriend();
            }
        });
        removeFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveFriend();
            }
        });
        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelRequest();
            }
        });
        acceptRequestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAcceptRequest();
            }
        });
        declineRequestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDeclineRequest();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.user_view_recycler_view);
        recyclerView.setHasFixedSize(true);
        uvnra = new UserViewNoteRecyclerAdapter(user.getNotes(), this);
        recyclerView.setAdapter(uvnra);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAddFriend() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_friend)
                .setMessage("Do you want to add " + user.getFirstName() + " as a friend?")
                .setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.requestAsFriend(loggedUser, user);
                        changeViewToCancelRequest();
                        Toast.makeText(getApplicationContext(), "Sent a friend request to " + user.getFirstName(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.textCancel, null)
                .show();
    }

    public void showRemoveFriend() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.remove_friend)
                .setMessage("Do you want to remove " + user.getFirstName() + " as a friend?")
                .setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.removeAsFriend(loggedUser, user);
                        changeViewToAdd();
                        Toast.makeText(getApplicationContext(), "Removed " + user.getFirstName() + " as a friend", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.textCancel, null)
                .show();
    }

    public void showCancelRequest() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.cancel_friend_request)
                .setMessage("Do you want to cancel your friend request to " + user.getFirstName() + "?")
                .setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.cancelFriendRequest(loggedUser, user);

                        changeViewToAdd();
                        Toast.makeText(getApplicationContext(), "Cancelled your friend request to " + user.getFirstName(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.textBack, null)
                .show();
    }

    public void showAcceptRequest() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.accept_request)
                .setMessage("Do you want to accept the friend request of " + user.getFirstName() + "?")
                .setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.acceptFriendRequest(loggedUser, user);

                        changeViewToRemove();
                        Toast.makeText(getApplicationContext(), "Accepted the friend request of " + user.getFirstName(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.textBack, null)
                .show();
    }

    public void showDeclineRequest() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.decline_request)
                .setMessage("Do you want to decline the friend request of " + user.getFirstName() + "?")
                .setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.declineFriendRequest(loggedUser, user);

                        changeViewToAdd();
                        Toast.makeText(getApplicationContext(), "Declined the friend request of " + user.getFirstName(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.textBack, null)
                .show();
    }

    public void changeViewToAdd(){
        addFriendButton.setVisibility(View.VISIBLE);
        friendContainer.setVisibility(View.GONE);
    }

    public void changeViewToRemove(){
        addFriendButton.setVisibility(View.GONE);
        friendContainer.setVisibility(View.VISIBLE);
        friendText.setText("You're Friends with " + user.getFirstName());
        removeFriendButton.setVisibility(View.VISIBLE);
        requestContainer.setVisibility(View.GONE);
        cancelRequestButton.setVisibility(View.GONE);
    }

    public void changeViewToCancelRequest(){
        addFriendButton.setVisibility(View.GONE);
        friendContainer.setVisibility(View.VISIBLE);
        friendText.setText("You have sent a friend request to " + user.getFirstName());
        removeFriendButton.setVisibility(View.GONE);
        requestContainer.setVisibility(View.GONE);
        cancelRequestButton.setVisibility(View.VISIBLE);
    }

    public void changeViewToRespondRequest(){
        addFriendButton.setVisibility(View.GONE);
        friendContainer.setVisibility(View.VISIBLE);
        friendText.setText(user.getFirstName() + " sent you a friend request");
        removeFriendButton.setVisibility(View.GONE);
        requestContainer.setVisibility(View.VISIBLE);
        cancelRequestButton.setVisibility(View.GONE);
    }
}
