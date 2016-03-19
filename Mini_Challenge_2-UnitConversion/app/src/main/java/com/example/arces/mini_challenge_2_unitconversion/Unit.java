package com.example.arces.mini_challenge_2_unitconversion;

import java.io.Serializable;

/**
 * Created by Arces on 16/03/2016.
 */
public class Unit implements Serializable{
    private int unitID;
    private String name;
    private String category;

    public Unit(String name, String category){
        this.name = name;
        this.category = category;
    }

    public Unit(int unitID, String name, String category){
        this.unitID = unitID;
        this.name = name;
        this.category = category;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
