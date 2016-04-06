package com.logmedown.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.logmedown.model.Bloc;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Arces on 22/02/2016.
 */
public class Database extends SQLiteOpenHelper{
    private static Database dbInstance;
    private static final String db_name = "LogMeDown.db";
    private static final int db_version = 1;

    // -- TABLES ----------------
    private static final String user_table = "user";
    private static final String bloc_table = "bloc";
    private static final String note_table = "note";
    private static final String friend_table = "friend";
    private static final String add_as_friend_table = "add_as_friend";
    private static final String bloc_member_table = "bloc_member";
    private static final String invite_user_to_bloc_table = "invite_user_to_bloc";
    private static final String request_to_join_table = "request_to_join";

    public static Database getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new Database(context.getApplicationContext());
        }

        return dbInstance;
    }

    private Database(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create User Table
        String CREATE_USER_TABLE = "CREATE TABLE " + user_table + "(userID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, firstName TEXT NOT NULL, " +
                "lastName TEXT NOT NULL, emailAddress TEXT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL)";
        db.execSQL(CREATE_USER_TABLE);

        //Create Bloc Table
        String CREATE_BLOC_TABLE = "CREATE TABLE " + bloc_table + "(blocID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, creatorID INTEGER NOT NULL, " +
                "name TEXT NOT NULL, type TEXT NOT NULL, isDeleted BINARY NOT NULL)";
        db.execSQL(CREATE_BLOC_TABLE);

        //Create Note Table
        String CREATE_NOTE_TABLE = "CREATE TABLE " + note_table + "(noteID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, creatorID INTEGER NOT NULL, " +
                "blocID INTEGER, title TEXT NOT NULL, content TEXT NOT NULL, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL)";
        db.execSQL(CREATE_NOTE_TABLE);

        //Create Friend Table
        String CREATE_FRIEND_TABLE = "CREATE TABLE " + friend_table + "(friend1 INTEGER NOT NULL, friend2 INTEGER NOT NULL)";
        db.execSQL(CREATE_FRIEND_TABLE);

        //Create Add As Friend Table
        String CREATE_ADD_FRIEND_TABLE = "CREATE TABLE " + add_as_friend_table + "(adderID INTEGER NOT NULL, addedID INTEGER NOT NULL, status TEXT NOT NULL)";
        db.execSQL(CREATE_ADD_FRIEND_TABLE);

        //Create Add User To Bloc Table
        String CREATE_BLOC_MEMBER_TABLE = "CREATE TABLE " + bloc_member_table + "(memberID INTEGER NOT NULL, blocID INTEGER NOT NULL)";
        db.execSQL(CREATE_BLOC_MEMBER_TABLE);

        //Create Request To Join Bloc Table
        String CREATE_REQUEST_TO_JOIN_BLOC_TABLE = "CREATE TABLE " + request_to_join_table + "(ownerID INTEGER NOT NULL, blocID INTEGER NOT NULL, requesterID INTEGER NOT NULL, status TEXT NOT NULL)";
        db.execSQL(CREATE_REQUEST_TO_JOIN_BLOC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + user_table);
        onCreate(db);
    }

    public void addUser(User user) {
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
        User user = null;
        Cursor cursor = db.query(true, user_table, null, "username = ? AND password = ?", new String[]{username, password}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            int userID = cursor.getInt(cursor.getColumnIndex("userID"));
            cursor.close();
            user = getUser(userID);
        }
        else{
            cursor.close();
        }
        return user;
    }

    public int getLastNoteId(){
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;

        Cursor cursor = db.query(note_table,new String [] {"MAX(noteID)"}, null, null, null, null, null);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();

            id = cursor.getInt(cursor.getColumnIndex("MAX(noteID)"));
            cursor.close();
        }

        return id;
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues val = new ContentValues();
        val.put("title", note.getTitle());
        val.put("creatorID", note.getCreator().getUserID());

        if (note.getBloc() == null)
            val.put("blocID", -1);
        else
            val.put("blocID", note.getBloc().getBlocID());

        val.put("content", note.getContent());

        db.insert(note_table, null, val);

        //Log.d("note_add", "note added!: " + note.getTitle() + " " + note.getContent() + " by " + note.getCreator().getFirstName() + " " + note.getBloc().getName());
        db.close();
    }

    public ArrayList<Note> getNotes(User user) {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, note_table, null, "creatorID = ?", new String[]{Integer.toString(user.getUserID())}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Note note = new Note();
                int noteID = cursor.getInt(cursor.getColumnIndex("noteID"));
                int blocID = cursor.getInt(cursor.getColumnIndex("blocID"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Date date = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("date")));

                note.setNoteID(noteID);
                note.setCreator(user);

                if(blocID == -1) {
                    note.setBloc(null);
                    Log.d("get_note", note.getTitle() + " by " + note.getCreator().getFirstName() + " on " + note.getDate() + " null");
                }
                else {
                    Bloc bloc = new Bloc();
                    bloc.setBlocID(blocID);
                    fillBlocDetails(bloc, user);
                    note.setBloc(bloc);
                    Log.d("get_note", note.getTitle() + " by " + note.getCreator().getFirstName() + " on " + note.getDate() + " " + note.getBloc().getName());
                }

                note.setTitle(title);
                note.setContent(content);
                note.setDate(date);

                notes.add(note);
                cursor.moveToNext();
            }
            cursor.close();
        }
        else {
            cursor.close();
        }
        return notes;
    }

    public ArrayList<Note> getNotes(Bloc bloc) {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, note_table, null, "blocID = ?", new String[]{Integer.toString(bloc.getBlocID())}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Note note = new Note();
                int noteID = cursor.getInt(cursor.getColumnIndex("noteID"));
                int blocID = cursor.getInt(cursor.getColumnIndex("blocID"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Date date = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("date")));

                note.setNoteID(noteID);
                note.setCreator(bloc.getCreator());

                if(blocID == -1)
                    note.setBloc(null);
                else
                    note.setBloc(bloc); //later

                note.setTitle(title);
                note.setContent(content);
                note.setDate(date);

                Log.d("get_note", note.getTitle() + " by " + note.getCreator().getFirstName() + " on " + note.getDate());
                notes.add(note);
                cursor.moveToNext();
            }
            cursor.close();
        }
        else {
            cursor.close();
        }
        return notes;
    }

    public void fillUserDetails(User user){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, user_table, null, "userID = ?", new String[]{Integer.toString(user.getUserID())}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            user.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("lastName")));
            user.setEmailAddress(cursor.getString(cursor.getColumnIndex("emailAddress")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));

            cursor.close();
        }
        else{
            cursor.close();
        }
    }

    public void fillBlocDetails(Bloc bloc, User user){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, bloc_table, null, "blocID = ?", new String[]{Integer.toString(bloc.getBlocID())}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            bloc.setBlocID(cursor.getInt(cursor.getColumnIndex("blocID")));
            bloc.setName(cursor.getString(cursor.getColumnIndex("name")));
            bloc.setType(cursor.getString(cursor.getColumnIndex("type")));
            bloc.setCreator(user);

        }
        cursor.close();
        db.close();

    }

    public ArrayList<User> getFriends(User user){
        ArrayList<User> friends = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, friend_table, null, "friend1 = ? or friend2 = ?", new String[]{Integer.toString(user.getUserID()), Integer.toString(user.getUserID())}, null, null, null, null);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false){
                User friend = new User();

                if(cursor.getInt(cursor.getColumnIndex("friend1")) == user.getUserID()){
                    friend.setUserID(cursor.getInt(cursor.getColumnIndex("friend2")));
                }else{
                    friend.setUserID(cursor.getInt(cursor.getColumnIndex("friend1")));
                }

                fillUserDetails(friend);

                friends.add(friend);
                cursor.moveToNext();
            }
            cursor.close();
        } else{
            cursor.close();
        }

        return friends;
    }

    public User getCreatorOfBloc(Bloc bloc){
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, bloc_table, null, "blocID = ?", new String[]{Integer.toString(bloc.getBlocID())}, null, null, null, null);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            user.setUserID(cursor.getInt(cursor.getColumnIndex("creatorID")));
            fillUserDetails(user);
        }
        cursor.close();

        return user;
    }

    public ArrayList<User> getMembersOfBloc(Bloc bloc){
        ArrayList<User> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, bloc_member_table, null, "blocID = ?", new String[]{Integer.toString(bloc.getBlocID())}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                User user = new User();
                user.setUserID(cursor.getInt(cursor.getColumnIndex("memberID")));
                fillUserDetails(user);
                Log.d("db.membersofbloc", user.getFirstName() + " " + user.getLastName());

                members.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }
        else{
            cursor.close();
        }
        return members;
    }

    public ArrayList<Bloc> getBlocs(User user){
        ArrayList<Bloc> blocs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, bloc_table, null, "creatorID = ?", new String[]{Integer.toString(user.getUserID())}, null, null, null, null);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false){
                Bloc bloc  = new Bloc();

                bloc.setBlocID(cursor.getInt(cursor.getColumnIndex("blocID")));
                bloc.setName(cursor.getString(cursor.getColumnIndex("name")));
                bloc.setType(cursor.getString(cursor.getColumnIndex("type")));
                bloc.setCreator(user);

                bloc.setMembers(getMembersOfBloc(bloc));
                bloc.setNotes(getNotes(bloc));

                blocs.add(bloc);
                cursor.moveToNext();
            }
            cursor.close();
        } else{
            cursor.close();
        }

        return blocs;
    }

    public User getUser(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.query(true, user_table, null, "userID = ?", new String[]{Integer.toString(userID)}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            user = new User();

            user.setUserID(cursor.getInt(cursor.getColumnIndex("userID")));
            user.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("lastName")));
            user.setEmailAddress(cursor.getString(cursor.getColumnIndex("emailAddress")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));

            cursor.close();
            user.setNotes(getNotes(user));
            user.setFriends(getFriends(user));
            user.setBlocs(getBlocs(user));

            Log.d("get_user", "id = " + user.getUserID() + " ; name = " + user.getFirstName() + " " + user.getLastName() +
                    " ; email = " + user.getEmailAddress() + " ; username = " + user.getUsername() + " ; password = " + user.getPassword());
        }
        else{
            cursor.close();
        }
        return user;
    }

    public void editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("title", note.getTitle());
        val.put("content", note.getContent());

        db.update(note_table, val, "noteID = " + note.getNoteID(), null);
        db.close();
    }

    public void deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Delete db", String.valueOf(note.getNoteID()));

        db.delete(note_table, "noteID = " + note.getNoteID(), null);
        db.close();
    }

    public void deleteBloc(Bloc bloc){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(bloc_table, "blocID = " + bloc.getBlocID(), null);

        for(int i = 0; i < bloc.getMembers().size(); i++){
            db.delete(bloc_member_table, "memberID = ? and blocID = ?", new String[]{String.valueOf(bloc.getMembers().get(i).getUserID()), String.valueOf(bloc.getBlocID())});
        }

        for(int i = 0; i < bloc.getNotes().size(); i++){
            db.delete(note_table, "blocID = ?", new String[]{String.valueOf(bloc.getBlocID())});
        }
        db.close();
    }

    public void deleteMemberFromBloc(Bloc bloc, User user){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(bloc_member_table, "memberID = ? and blocID = ?", new String[]{String.valueOf(user.getUserID()), String.valueOf(bloc.getBlocID())});
        db.close();
    }

    public void addRequestToJoin(Bloc bloc, User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues val = new ContentValues();
        val.put("ownerID", bloc.getCreator().getUserID());
        val.put("blocID", bloc.getBlocID());
        val.put("requesterID", user.getUserID());
        val.put("status", "Pending");

        db.insert(request_to_join_table, null, val);
        db.close();
    }

    public void deleteRequestToJoin(Bloc bloc, User user){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(request_to_join_table, "blocID = ? and requesterID = ?", new String[]{String.valueOf(bloc.getBlocID()), String.valueOf(user.getUserID())});
        db.close();
    }

    public void addMembersToBloc(Bloc bloc){
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i = 0; i < bloc.getMembers().size(); i++){
            ContentValues val = new ContentValues();
            val.put("memberID", bloc.getMembers().get(i).getUserID());
            val.put("blocID", bloc.getBlocID());
            db.insert(bloc_member_table, null, val);
        }
        db.close();
    }

    public boolean hasPendingRequestToJoin(Bloc bloc, User user){
        boolean hasPending = false;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, request_to_join_table, null, "blocID = ? and requesterID = ? and status = ?", new String[]{String.valueOf(bloc.getBlocID()), String.valueOf(user.getUserID()), "Pending"}, null, null, null, null);

        if(cursor.getCount() != 0){
            hasPending = true;
        }

        return hasPending;
    }

    public void addMemberToBloc(Bloc bloc, User member){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put("memberID",member.getUserID());
        val.put("blocID", bloc.getBlocID());
        db.insert(bloc_member_table, null, val);

        db.close();
    }

    public void addBloc(Bloc bloc){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues val = new ContentValues();
        val.put("creatorID", bloc.getCreator().getUserID());
        val.put("name", bloc.getName());
        val.put("type", bloc.getType());
        val.put("isDeleted", false);

        Log.d("ADD BLOC DB", bloc.getName().toString());

        db.insert(bloc_table, null, val);
        for(int i = 0; i < bloc.getMembers().size(); i++){
            Log.d("ADD BLOC DB MEM", bloc.getMembers().get(i).getFirstName());
            addMemberToBloc(bloc, bloc.getMembers().get(i));
        }
        db.close();
    }

    public ArrayList<Note> searchNotes(String keyword){
        ArrayList<Note> notes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, note_table, null, "title LIKE ?", new String[]{"%" + keyword + "%"}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Note note = new Note();
                int noteID = cursor.getInt(cursor.getColumnIndex("noteID"));
                int blocID = cursor.getInt(cursor.getColumnIndex("blocID"));
                int userID = cursor.getInt(cursor.getColumnIndex("creatorID"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Date date = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("date")));

                note.setNoteID(noteID);
                User user = new User();
                user.setUserID(userID);
                fillUserDetails(user);
                note.setCreator(user);

                if (blocID == -1)
                    note.setBloc(null);

                note.setTitle(title);
                note.setContent(content);
                note.setDate(date);

//                Log.d("search_note", note.getTitle() + " by " + note.getCreator().getFirstName() + " on " + note.getDate());
                notes.add(note);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return notes;
    }

    public ArrayList<Bloc> searchBlocs(String keyword){
        ArrayList<Bloc> blocs = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //apply privacy in query later
        Cursor cursor = db.query(true, bloc_table, null, "name LIKE ? AND isDeleted = ?", new String[]{"%" + keyword + "%", Integer.toString(0)}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Bloc bloc = new Bloc();
                int blocID = cursor.getInt(cursor.getColumnIndex("blocID"));
                int userID = cursor.getInt(cursor.getColumnIndex("creatorID"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String type = cursor.getString(cursor.getColumnIndex("type"));

                bloc.setBlocID(blocID);
                bloc.setCreator(getUser(userID));
                bloc.setName(name);
                bloc.setType(type);

                Log.d("search_bloc", bloc.getName() + " by " + bloc.getCreator().getFirstName() + " of type " + bloc.getType());
                blocs.add(bloc);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return blocs;
    }

    public ArrayList<User> searchUsers(String keyword){
        ArrayList<User> users = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //apply privacy in query later
        Cursor cursor = db.query(true, user_table, new String[]{"userID"}, "firstName||' '||lastName LIKE ?", new String[]{"%" + keyword + "%"}, null, null, null, null);

        //Log.d("search_user", keyword);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                int userID = cursor.getInt(cursor.getColumnIndex("userID"));
                User user = getUser(userID);

              //  Log.d("search_user", user.getFirstName() + " " + user.getLastName());
                users.add(user);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return users;
    }

    public void requestAsFriend(User adder, User added){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put("adderID", adder.getUserID());
        val.put("addedID", added.getUserID());
        val.put("status", "Pending");

        db.insert(add_as_friend_table, null, val);
    }

    public boolean isFriend(User friend1, User friend2){
        int friend1ID = friend1.getUserID();
        int friend2ID = friend2.getUserID();
        boolean isFriend = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(true, friend_table, null, "friend1 = ? AND friend2 = ?", new String[]{Integer.toString(friend1ID), Integer.toString(friend2ID)}, null, null, null, null);
        if(cursor.getCount() != 0){
            isFriend = true;
        }
        if(!isFriend){
            cursor = db.query(true, friend_table, null, "friend1 = ? AND friend2 = ?", new String[]{Integer.toString(friend2ID), Integer.toString(friend1ID)}, null, null, null, null);
            if(cursor.getCount() != 0){
                isFriend = true;
            }
        }
        return isFriend;
    }

    public boolean hasFriendRequest(User adder, User added){
        int adderID = adder.getUserID();
        int addedID = added.getUserID();
        boolean hasRequest = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(true, add_as_friend_table, null, "adderID = ? AND addedID = ? AND status = ?", new String[]{Integer.toString(adderID), Integer.toString(addedID), "Pending"}, null, null, null, null);

        if(cursor.getCount() != 0){
            hasRequest = true;
        }

        return hasRequest;
    }

    public void cancelFriendRequest(User adder, User added){
        int adderID = adder.getUserID();
        int addedID = added.getUserID();
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(add_as_friend_table, "adderID = ? and addedID = ?", new String[]{Integer.toString(adderID), Integer.toString(addedID)});
        db.close();
    }

    public void acceptFriendRequest(User added, User adder){
        int adderID = adder.getUserID();
        int addedID = added.getUserID();
        SQLiteDatabase db = this.getWritableDatabase();

        //update friend request -> Accepted
        ContentValues val = new ContentValues();
        val.put("status", "Accepted");

        db.update(add_as_friend_table, val, "adderID = " + adderID + " AND addedID = " + addedID, null);
        val.clear();

        //add friendship to friend table
        val.put("friend1", adderID);
        val.put("friend2", addedID);
        db.insert(friend_table, null, val);
        db.close();
    }

    public void declineFriendRequest(User added, User adder){
        int adderID = adder.getUserID();
        int addedID = added.getUserID();
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(add_as_friend_table, "addedID = " + addedID + " AND adderID = " + adderID, null);
        db.close();
    }

    public void removeAsFriend(User friend1, User friend2){
        int friend1ID = friend1.getUserID();
        int friend2ID = friend2.getUserID();
        SQLiteDatabase db = this.getWritableDatabase();

        //delete from friend table
        db.delete(friend_table, "friend1 = " + friend1ID + " AND friend2 = " + friend2ID, null);
        db.delete(friend_table, "friend1 = " + friend2ID + " AND friend2 = " + friend1ID, null);

        //delete accepted request
        db.delete(add_as_friend_table, "adderID = " + friend1ID + " AND addedID = " + friend2ID + " AND status = 'Accepted'", null);
        db.delete(add_as_friend_table, "adderID = " + friend2ID + " AND addedID = " + friend1ID + " AND status = 'Accepted'", null);
        db.close();
    }
}
