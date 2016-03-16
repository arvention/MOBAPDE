package com.example.arces.mini_challenge_2_unitconversion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arces on 14/03/2016.
 */
public class Database extends SQLiteOpenHelper {
    private static Database dbInstance;
    private static final String db_name = "UnitConversion.db";
    private static final int db_version = 1;

    //-- TABLES ------
    private static final String unit_table = "unit";
    private static final String conversion_table = "conversion";

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
        String CREATE_UNIT_TABLE = "CREATE TABLE " + unit_table + "(unitID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, category TEXT NOT NULL);";
        db.execSQL(CREATE_UNIT_TABLE);

        String CREATE_CONVERSION_TABLE = "CREATE TABLE " + conversion_table + "(unitID INTEGER NOT NULL, value TEXT NOT NULL, resultID INTEGER NOT NULL);";
        db.execSQL(CREATE_CONVERSION_TABLE);

        initializeData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initializeData(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues val = new ContentValues();

    //-- ADD UNITS ------------
        //add distances
        val.put("name", "km");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        val.put("name", "mile");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        val.put("name", "m");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        val.put("name", "ft");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        //add weights
        val.put("name", "kg");
        val.put("category", "weight");
        db.insert(unit_table, null, val);

        val.put("name", "lb");
        val.put("category", "weight");
        db.insert(unit_table, null, val);

        val.put("name", "litre");
        val.put("category", "weight");
        db.insert(unit_table, null, val);

        val.put("name", "gal");
        val.put("category", "weight");
        db.insert(unit_table, null, val);

        //add area?
        val.put("name", "km2");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        val.put("name", "acre");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        val.put("name", "m2");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        val.put("name", "ft2");
        val.put("category", "distance");
        db.insert(unit_table, null, val);

        //add temperature
        val.put("name", "ºC");
        val.put("category", "temperature");
        db.insert(unit_table, null, val);

        val.put("name", "ºF");
        val.put("category", "temperature");
        db.insert(unit_table, null, val);

    //-- ADD CONVERSION -------------
        //-- Distance --------
        //km -> mile
        val.put("unitID", 1);
        val.put("value", 0.6214);
        val.put("resultID", 2);

        //mile -> km
        val.put("unitID", 2);
        val.put("value", 1.6093);
        val.put("resultID", 1);

        //m -> ft
        val.put("unitID", 3);
        val.put("value", 3.2808);
        val.put("resultID", 4);

        //ft -> m
        val.put("unitID", 4);
        val.put("value", 0.3048);
        val.put("resultID", 3);

        //-- Weight --------
        //kg -> lb
        val.put("unitID", 5);
        val.put("value", 2.2046);
        val.put("resultID", 6);

        //lb -> kg
        val.put("unitID", 6);
        val.put("value", 0.4536);
        val.put("resultID", 5);

        //litre -> gal
        val.put("unitID", 7);
        val.put("value", 0.2642);
        val.put("resultID", 8);

        //gal -> litre
        val.put("unitID", 8);
        val.put("value", 3.7854);
        val.put("resultID", 7);

        //-- Area? ----------
        //km2 -> acre
        val.put("unitID", 9);
        val.put("value", 247.1054);
        val.put("resultID", 10);

        //acre -> km2
        val.put("unitID", 10);
        val.put("value", 0.004047);
        val.put("resultID", 9);

        //m2 -> ft2
        val.put("unitID", 11);
        val.put("value", 10.7639);
        val.put("resultID", 12);

        //ft2 -> m2
        val.put("unitID", 12);
        val.put("value", 0.0929);
        val.put("resultID", 11);

        //-- Temperature ----------
        //ºC -> ºF
        val.put("unitID", 13);
        val.put("value", "(F - 32) * 1.8");
        val.put("resultID", 14);
        db.close();
    }
}
