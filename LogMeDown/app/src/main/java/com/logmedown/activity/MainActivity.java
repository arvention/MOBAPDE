package com.logmedown.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arces.logmedown.R;
import com.logmedown.database.Database;
import com.logmedown.model.User;


public class MainActivity extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    private static final String stringUsernameTag = "un";
    private static final String stringPasswordTag = "pw";
    //private static final String LOGGEDUSER = "loggedUser";
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

        editTextLoginUsername = (EditText) findViewById(R.id.edit_text_login_username);
        editTextLoginPassword = (EditText) findViewById(R.id.edit_text_login_password);
        buttonLogin = (Button) findViewById(R.id.button_log_in);
        buttonSignup = (Button) findViewById(R.id.button_sign_up);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextLoginUsername.getText().toString().equals("") && !editTextLoginPassword.getText().toString().equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    User loggedUser = logInUser();
                    if(loggedUser != null) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Welcome " + loggedUser.getFirstName(), Toast.LENGTH_SHORT);
                        toast.show();

                        finish();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("logged_user", loggedUser);
                        startActivity(intent);

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
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

      if(sharedPreferences.contains(stringUsernameTag) && sharedPreferences.contains(stringPasswordTag)) {
          String username = sharedPreferences.getString(stringUsernameTag, "");
          String password = sharedPreferences.getString(stringPasswordTag, "");

          User loggedUser = db.logInUser(username, password);
          (Toast.makeText(getApplicationContext(), "Welcome " + loggedUser.getFirstName() + ".", Toast.LENGTH_SHORT)).show();

          Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
          intent.putExtra("logged_user", loggedUser);
          startActivity(intent);
      }
    }

    /*@Override
    protected void onStop(){
        super.onStop();
        finish();
    }*/

    public User logInUser(){
        String username = editTextLoginUsername.getText().toString();
        String password = editTextLoginPassword.getText().toString();

        User loggedUser = db.logInUser(username, password);

        return loggedUser;
    }
}
