package com.example.arces.logmedown;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arces on 22/02/2016.
 */
public class Database extends SQLiteOpenHelper{
    private static final String db_name = "LogMeDown.db";
    private static final int db_version = 1;

    // -- TABLES ----------------
    private static final String user_table = "user";
    private static final String group_table = "group";
    private static final String note_table = "note";
    private static final String friend_table = "friend";
    private static final String add_as_friend_table = "add_as_friend";
    private static final String add_user_to_group_table = "add_user_to_group";
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

        //Create Group Table
        String CREATE_GROUP_TABLE = "CREATE TABLE " + group_table + "(groupID INTEGER PRIMARY KEY NOT NULL, userID INTEGER NOT NULL, " +
                "name TEXT NOT NULL, type TEXT NOT NULL, isDeleted BINARY NOT NULL)";
        db.execSQL(CREATE_GROUP_TABLE);

        //Create Note Table
        String CREATE_NOTE_TABLE = "CREATE TABLE " + note_table + "(noteID INTEGER PRIMARY KEY NOT NULL, userID INTEGER NOT NULL, " +
                "groupID INTEGER NOT NULL, title TEXT NOT NULL, content TEXT NOT NULL, timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL)";
        db.execSQL(CREATE_NOTE_TABLE);

        //Create Friend Table
        String CREATE_FRIEND_TABLE = "CREATE TABLE ";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
