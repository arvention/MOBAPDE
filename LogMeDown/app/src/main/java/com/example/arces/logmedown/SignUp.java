package com.example.arces.logmedown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    private EditText editTextSignUpFirstName;
    private EditText editTextSignUpLastName;
    private EditText editTextSignUpEmail;
    private EditText editTextSignUpUsername;
    private EditText editTextSignUpPassword;

    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextSignUpFirstName = (EditText)findViewById(R.id.editTextSignUpFirstName);
        editTextSignUpLastName = (EditText)findViewById(R.id.editTextSignUpLastName);
        editTextSignUpEmail = (EditText)findViewById(R.id.editTextSignUpEmailAddress);
        editTextSignUpUsername = (EditText)findViewById(R.id.editTextLoginUsername);
        editTextSignUpPassword = (EditText)findViewById(R.id.editTextSignUpPassword);

        buttonSignUp = (Button)findViewById(R.id.buttonSignUp);

    }
}
