package com.logmedown.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arces.logmedown.R;
import com.logmedown.database.Database;
import com.logmedown.model.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextSignUpFirstName;
    private EditText editTextSignUpLastName;
    private EditText editTextSignUpEmail;
    private EditText editTextSignUpUsername;
    private EditText editTextSignUpPassword;

    private Button buttonSignUp;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = Database.getInstance(this);

        editTextSignUpFirstName = (EditText) findViewById(R.id.edit_text_sign_up_first_name);
        editTextSignUpLastName = (EditText) findViewById(R.id.edit_text_sign_up_last_name);
        editTextSignUpEmail = (EditText) findViewById(R.id.edit_text_sign_up_email_address);
        editTextSignUpUsername = (EditText) findViewById(R.id.edit_text_sign_up_username);
        editTextSignUpPassword = (EditText) findViewById(R.id.edit_text_sign_up_password);

        buttonSignUp = (Button) findViewById(R.id.button_sign_up);
    }

    public void signUpUser(View view) {
        User user = new User();

        user.setFirstName(editTextSignUpFirstName.getText().toString());
        user.setLastName(editTextSignUpLastName.getText().toString());
        user.setEmailAddress(editTextSignUpEmail.getText().toString());
        user.setUsername(editTextSignUpUsername.getText().toString());
        user.setPassword(editTextSignUpPassword.getText().toString());
        db.addUser(user);

        (Toast.makeText(getApplicationContext(), "Welcome to LogMeDown, " + user.getFirstName() + " " + user.getLastName() + "!", Toast.LENGTH_SHORT)).show();
        finish();
    }
}
