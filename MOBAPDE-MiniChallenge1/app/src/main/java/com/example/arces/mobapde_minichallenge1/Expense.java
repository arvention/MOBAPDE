package com.example.arces.mobapde_minichallenge1;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public Date getDate() {;return date;}

    public void setDate(Date date) {
        this.date = date;
    }

    public String toCSVString(){
        return name + "," + String.valueOf(price) + "," + String.valueOf(date);
    }

    public void toExpenseObject(String csvString){
        String[] separatedString = csvString.split(",");
        this.name = separatedString[0];
        this.price = Float.valueOf(separatedString[1]);
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        try {
            this.date = df.parse(separatedString[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
