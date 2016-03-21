package com.logmedown.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Arces on 23/02/2016.
 */
public class User implements Serializable{
    private int userID;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String username;
    private String password;
    private ArrayList<Note> notes;
    private ArrayList<User> friends;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public ArrayList<User> getFriends(){ return friends; }

    public void setFriends(ArrayList<User> friends){ this.friends = friends; }

    public void addNote(Note note){
        notes.add(note);
    }

    public Note getNoteAt(int position){
        return notes.get(position);
    }

    public void deleteNote(int position){
        notes.remove(position);
    }
}
