package com.example.arces.logmedown;

import java.util.ArrayList;

/**
 * Created by Arces on 23/02/2016.
 */
public class Bloc {
    private int blocID;
    private String name;
    private String type;
    private ArrayList<User> members;

    public int getBlocID() {
        return blocID;
    }

    public void setBlocID(int blocID) {
        this.blocID = blocID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
}
