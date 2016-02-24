package com.example.arces.logmedown;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Arces on 23/02/2016.
 */
public class Note {
    private int noteID;
    private User creator;
    private String title;
    private String content;
    private Date time;
    private ArrayList<Integer> viewRestriction;

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public ArrayList<Integer> getViewRestriction() {
        return viewRestriction;
    }

    public void setViewRestriction(ArrayList<Integer> viewRestriction) {
        this.viewRestriction = viewRestriction;
    }
}
