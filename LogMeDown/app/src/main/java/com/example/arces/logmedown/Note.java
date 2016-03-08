package com.example.arces.logmedown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Arces on 23/02/2016.
 */
public class Note implements Serializable{
    private int noteID;
    private User creator;
    private String title;
    private String content;
    private Date date;
    private Bloc bloc;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creatorID) {
        this.creator = creator;
    }

    public Bloc getBloc() {
        return bloc;
    }

    public void setBloc(Bloc bloc) {
        this.bloc = bloc;
    }
}