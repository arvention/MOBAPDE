package com.arrenmatthew.mobapde_sessionmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static EditText username, password;
    public static Button loginButton;
    public SharedPreferences sharedpreferences;
    public FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new FeedReaderDbHelper(this);

        this.username = (EditText)findViewById(R.id.username_field);
        this.password = (EditText)findViewById(R.id.password_field);
        this.loginButton = (Button)findViewById(R.id.log_in_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gets the data repository in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                Log.d("user", username.getText().toString());
                Log.d("pass", password.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_USERNAME, username.getText().toString());
                values.put(FeedReaderContract.FeedEntry.COLUMN_PASSWORD, password.getText().toString());

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(
                        FeedReaderContract.FeedEntry.TABLE_NAME,
                        null,
                        values);

                SQLiteDatabase rdb = mDbHelper.getReadableDatabase();

                // Define a projection that specifies which columns from the database
                // you will actually use after this query.
                String[] projection = {
                        FeedReaderContract.FeedEntry.COLUMN_USERID,
                        FeedReaderContract.FeedEntry.COLUMN_USERNAME,
                        FeedReaderContract.FeedEntry.COLUMN_PASSWORD,
                };

                // How you want the results sorted in the resulting Cursor
                String sortOrder = FeedReaderContract.FeedEntry.COLUMN_USERID + " DESC";

                String selection = FeedReaderContract.FeedEntry.COLUMN_USERNAME + " = ?";
                String[] selectionArgs = new String[]{
                        username.getText().toString()
                };

                Cursor c = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,                      // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );

                c.moveToFirst();

                Log.d("ID: ", c.getString(0));
                Log.d("USERNAME: ", c.getString(1));
                Log.d("PASSWORD: ", c.getString(2));
            }
        });


        //sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        /*loginButton.setOnClickListener(new View.OnClickListener(){

        });*/
    }
}
