package com.example.arces.logmedown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    private static final String stringUsernameTag = "un";
    private static final String stringPasswordTag = "pw";
    private static final String LOGGEDUSER = "loggedUser";
    public SharedPreferences sharedPreferences;

    private EditText editTextLoginUsername;
    private EditText editTextLoginPassword;
    private Button buttonLogin;
    private Button buttonSignup;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deleteDatabase("LogMeDown.db");

        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);

        /*
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        */
        db = Database.getInstance(this);

        editTextLoginUsername = (EditText) findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogIn);
        buttonSignup = (Button) findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextLoginUsername.getText().toString().equals("") && !editTextLoginPassword.getText().toString().equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    User user = logInUser();
                    if(user != null) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT);
                        toast.show();

                        startActivity(new Intent(getApplicationContext(), Home.class));

                        editor.putString(stringUsernameTag, editTextLoginUsername.getText().toString());
                        editor.putString(stringPasswordTag, editTextLoginPassword.getText().toString());

                        editor.commit();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid username / password. Please try again.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    editTextLoginUsername.setText("", EditText.BufferType.EDITABLE);
                    editTextLoginPassword.setText("", EditText.BufferType.EDITABLE);
                }
                else if(editTextLoginUsername.getText().toString().equals("") && editTextLoginPassword.getText().toString().equals("")){
                    (Toast.makeText(getApplicationContext(), "Please enter username and password.", Toast.LENGTH_SHORT)).show();
                }
                else if(editTextLoginUsername.getText().toString().equals("")){
                    (Toast.makeText(getApplicationContext(), "Please enter username.", Toast.LENGTH_SHORT)).show();
                }
                else if(editTextLoginPassword.getText().toString().equals("")){
                    (Toast.makeText(getApplicationContext(), "Please enter password.", Toast.LENGTH_SHORT)).show();
                }
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

      if(sharedPreferences.contains(stringUsernameTag) && sharedPreferences.contains(stringPasswordTag)) {
          Gson gson = new Gson();
          String json = sharedPreferences.getString(LOGGEDUSER, "");
          User user = gson.fromJson(json, User.class);
          (Toast.makeText(getApplicationContext(), "Welcome " + user.getFirstName() + ".", Toast.LENGTH_SHORT)).show();

          startActivity(new Intent(getApplicationContext(), Home.class));
      }
    }

    public User logInUser(){
        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = editTextLoginUsername.getText().toString();
        String password = editTextLoginPassword.getText().toString();

        User user = db.logInUser(username, password);

        if(user != null){
            Gson gson = new Gson();
            String json = gson.toJson(user);
            editor.putString(LOGGEDUSER, json);
            editor.commit();
        }
        return user;
    }
}
