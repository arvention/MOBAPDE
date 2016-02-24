package com.example.arces.logmedown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    private static final String stringUsernameTag = "un";
    private static final String stringPasswordTag = "pw";
    public SharedPreferences sharedPreferences;

    private EditText editTextLoginUsername;
    private EditText editTextLoginPassword;
    private Button buttonLogin;
    private Button buttonSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(USERPREFERENCES, MODE_PRIVATE);

        editTextLoginUsername = (EditText) findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogIn);
        buttonSignup = (Button) findViewById(R.id.buttonSignUp);

        sharedPreferences = getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextLoginUsername.getText().toString().equals("") && !editTextLoginPassword.getText().toString().equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(stringUsernameTag, editTextLoginUsername.getText().toString());
                    editor.putString(stringPasswordTag, editTextLoginPassword.getText().toString());

                    editor.commit();

                    Toast toast = Toast.makeText(getApplicationContext(), "Welcome " + sharedPreferences.getString(stringUsernameTag, "fail"), Toast.LENGTH_SHORT);
                    toast.show();

                    startActivity(new Intent(getApplicationContext(), Home.class));
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
            Toast toast = Toast.makeText(getApplicationContext(), "Welcome " + sharedPreferences.getString(stringUsernameTag, "fail"), Toast.LENGTH_SHORT);
            toast.show();

            startActivity(new Intent(getApplicationContext(), Home.class));
        }
    }
}
