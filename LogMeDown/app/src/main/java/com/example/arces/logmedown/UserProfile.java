package com.example.arces.logmedown;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class UserProfile extends AppCompatActivity {
    private String USERPREFERENCES = "UserPreferences";
    private static final String LOGGEDUSER = "loggedUser";
    private TextView profileName, profileEmail, profileUsername, profilePassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profileName = (TextView) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profileUsername = (TextView) findViewById(R.id.profileUsername);
        profilePassword = (TextView) findViewById(R.id.profilePassword);

        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOGGEDUSER, "");
        User user = gson.fromJson(json, User.class);

        profileName.setText(user.getFirstName() + " " + user.getLastName());
        profileEmail.setText("( " + user.getEmailAddress() + " )");
        profileUsername.setText(user.getUsername());
        profilePassword.setText(user.getPassword());
    }
}
