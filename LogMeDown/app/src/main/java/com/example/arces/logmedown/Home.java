package com.example.arces.logmedown;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;

public class Home extends AppCompatActivity {

    private static final String USERPREFERENCES = "UserPreferences";
    public SharedPreferences sharedPreferences;

    private Button buttonLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
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
        });
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
}
