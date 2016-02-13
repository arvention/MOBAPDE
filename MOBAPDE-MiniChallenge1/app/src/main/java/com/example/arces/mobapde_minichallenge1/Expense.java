package com.example.arces.mobapde_minichallenge1;

import java.util.Date;

/**
 * Created by Arces on 13/02/2016.
 */
public class Expense {
    private String name;
    private float price;
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
