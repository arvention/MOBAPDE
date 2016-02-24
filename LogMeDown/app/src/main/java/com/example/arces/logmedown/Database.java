package com.example.arces.logmedown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Arces on 22/02/2016.
 */
public class Database extends SQLiteOpenHelper{
    private static final String db_name = "LogMeDown.db";
    private static final int db_version = 1;

    // -- TABLES ----------------
    private static final String user_table = "user";
    private static final String bloc_table = "bloc";
    private static final String note_table = "note";
    private static final String friend_table = "friend";
    private static final String add_as_friend_table = "add_as_friend";
    private static final String add_user_to_bloc_table = "add_user_to_bloc";
    private static final String request_to_join_table = "request_to_join";

    public Database(Context context){
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create User Table
        String CREATE_USER_TABLE = "CREATE TABLE " + user_table + "(userID INTEGER PRIMARY KEY NOT NULL, firstName TEXT NOT NULL, " +
                "lastName TEXT NOT NULL, emailAddress TEXT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL)";
        db.execSQL(CREATE_USER_TABLE);

        //Create Bloc Table
        String CREATE_BLOC_TABLE = "CREATE TABLE " + bloc_table + "(blocID INTEGER PRIMARY KEY NOT NULL, userID INTEGER NOT NULL, " +
                "name TEXT NOT NULL, type TEXT NOT NULL, isDeleted BINARY NOT NULL)";
        db.execSQL(CREATE_BLOC_TABLE);

        //Create Note Table
        String CREATE_NOTE_TABLE = "CREATE TABLE " + note_table + "(noteID INTEGER PRIMARY KEY NOT NULL, userID INTEGER NOT NULL, " +
                "blocID INTEGER NOT NULL, title TEXT NOT NULL, content TEXT NOT NULL, time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL)";
        db.execSQL(CREATE_NOTE_TABLE);

        //Create Friend Table
        String CREATE_FRIEND_TABLE = "CREATE TABLE ";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues val = new ContentValues();
        val.put("firstName", user.getFirstName());
        val.put("lastName", user.getLastName());
        val.put("emailAddress", user.getEmailAddress());
        val.put("username", user.getUsername());
        val.put("password", user.getPassword());

        db.insert(user_table, null, val);
        db.close();
    }

    public User logInUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        Cursor rs = db.query(true, user_table, null, "username = ? AND password = ?", new String[]{username, password}, null, null, null, null);

        if (rs != null) {
            rs.moveToFirst();

            user.setUserID(rs.getInt(0));
            user.setFirstName(rs.getString(1));
            user.setLastName(rs.getString(2));
            user.setEmailAddress(rs.getString(3));
            user.setUsername(rs.getString(4));
            user.setPassword(rs.getString(5));

            user.setNotes(null);
        }
        return user;
    }
}
