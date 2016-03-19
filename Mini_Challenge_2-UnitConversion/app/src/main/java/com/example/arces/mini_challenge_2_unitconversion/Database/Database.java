package com.example.arces.mini_challenge_2_unitconversion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.arces.mini_challenge_2_unitconversion.Unit;

import java.util.ArrayList;

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

        initializeData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initializeData(SQLiteDatabase db){
        ContentValues val = new ContentValues();

    //-- ADD UNITS ------------
        //add distances
        val.put("name", "km");
        val.put("category", "distance");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "mile");
        val.put("category", "distance");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "m");
        val.put("category", "distance");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "ft");
        val.put("category", "distance");
        db.insert(unit_table, null, val);
        val.clear();

        //add weights
        val.put("name", "kg");
        val.put("category", "weight");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "lb");
        val.put("category", "weight");
        db.insert(unit_table, null, val);

        //add volume
        val.put("name", "litre");
        val.put("category", "volume");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "gal");
        val.put("category", "volume");
        db.insert(unit_table, null, val);
        val.clear();

        //add area?
        val.put("name", "km2");
        val.put("category", "area");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "acre");
        val.put("category", "area");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "m2");
        val.put("category", "area");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "ft2");
        val.put("category", "area");
        db.insert(unit_table, null, val);
        val.clear();

        //add temperature
        val.put("name", "ºC");
        val.put("category", "temperature");
        db.insert(unit_table, null, val);
        val.clear();

        val.put("name", "ºF");
        val.put("category", "temperature");
        db.insert(unit_table, null, val);
        val.clear();


    //-- ADD CONVERSION -------------
        //-- Distance --------
        //km -> mile
        val.put("unitID", 1);
        val.put("value", 0.6214);
        val.put("resultID", 2);
        db.insert(conversion_table, null, val);
        val.clear();

        //mile -> km
        val.put("unitID", 2);
        val.put("value", 1.6093);
        val.put("resultID", 1);
        db.insert(conversion_table, null, val);
        val.clear();

        //km -> m
        val.put("unitID", 1);
        val.put("value", 1000);
        val.put("resultID", 3);
        db.insert(conversion_table, null, val);
        val.clear();

        //m -> km
        val.put("unitID", 3);
        val.put("value", 0.001);
        val.put("resultID", 1);
        db.insert(conversion_table, null, val);
        val.clear();

        //m -> ft
        val.put("unitID", 3);
        val.put("value", 3.2808);
        val.put("resultID", 4);
        db.insert(conversion_table, null, val);
        val.clear();

        //ft -> m
        val.put("unitID", 4);
        val.put("value", 0.3048);
        val.put("resultID", 3);
        db.insert(conversion_table, null, val);
        val.clear();

        //-- Weight --------
        //kg -> lb
        val.put("unitID", 5);
        val.put("value", 2.2046);
        val.put("resultID", 6);
        db.insert(conversion_table, null, val);
        val.clear();

        //lb -> kg
        val.put("unitID", 6);
        val.put("value", 0.4536);
        val.put("resultID", 5);
        db.insert(conversion_table, null, val);
        val.clear();

        //-- Volume ---------
        //litre -> gal
        val.put("unitID", 7);
        val.put("value", 0.2642);
        val.put("resultID", 8);
        db.insert(conversion_table, null, val);
        val.clear();

        //gal -> litre
        val.put("unitID", 8);
        val.put("value", 3.7854);
        val.put("resultID", 7);
        db.insert(conversion_table, null, val);
        val.clear();

        //-- Area ----------
        //km2 -> acre
        val.put("unitID", 9);
        val.put("value", 247.1054);
        val.put("resultID", 10);
        db.insert(conversion_table, null, val);
        val.clear();

        //acre -> km2
        val.put("unitID", 10);
        val.put("value", 0.004047);
        val.put("resultID", 9);
        db.insert(conversion_table, null, val);
        val.clear();

        //km2 -> m2
        val.put("unitID", 9);
        val.put("value", 1000000);
        val.put("resultID", 11);
        db.insert(conversion_table, null, val);
        val.clear();

        //m2 -> km2
        val.put("unitID", 11);
        val.put("value", 0.000001);
        val.put("resultID", 9);
        db.insert(conversion_table, null, val);
        val.clear();

        //m2 -> ft2
        val.put("unitID", 11);
        val.put("value", 10.7639);
        val.put("resultID", 12);
        db.insert(conversion_table, null, val);
        val.clear();

        //ft2 -> m2
        val.put("unitID", 12);
        val.put("value", 0.0929);
        val.put("resultID", 11);
        db.insert(conversion_table, null, val);
        val.clear();

        //-- Temperature ----------
        //ºC -> ºF
        val.put("unitID", 13);
        val.put("value", "ºC * 1.8 + 32");
        val.put("resultID", 14);
        db.insert(conversion_table, null, val);
        val.clear();

        //ºF -> ºC
        val.put("unitID", 14);
        val.put("value", "(ºF - 32) * 1.8");
        val.put("resultID", 13);
        db.insert(conversion_table, null, val);
        val.clear();
    }

    public ArrayList<Unit> getUnits(String category){
        ArrayList<Unit> unitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, unit_table, null, "category = ?", new String[]{category}, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(cursor.getColumnIndex("unitID"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                Unit unit = new Unit(id, name, category);
                unitList.add(unit);

                cursor.moveToNext();
            }

            cursor.close();
        }
        else{
            cursor.close();
        }

        return unitList;
    }

    public String getMultiplier(int fromID, int toID){
        String multiplier = "";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, conversion_table, null, "unitID = ? AND resultID = ?", new String[]{Integer.toString(fromID), Integer.toString(toID)}, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            multiplier = cursor.getString(cursor.getColumnIndex("value"));

            cursor.close();
        }
        else{
            cursor.close();
        }
        db.close();
        return multiplier;
    }

    public long addUnit(Unit existingUnit, String value, Unit unit){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues val = new ContentValues();

        val.put("name", unit.getName());
        val.put("category", unit.getCategory().toLowerCase());
        long id = db.insert(unit_table, null, val);
        val.clear();
        Log.d("add_unit", "name = " + unit.getName() + " category = " + unit.getCategory());

        val.put("unitID", existingUnit.getUnitID());
        val.put("value", value);
        val.put("resultID", id);
        db.insert(conversion_table, null, val);

        val.put("unitID", id);
        val.put("value", 1/Float.parseFloat(value));
        val.put("resultID", existingUnit.getUnitID());
        db.insert(conversion_table, null, val);
        Log.d("add_unit_conversion", "existing = " + existingUnit.getName() + " value = " + value + " unit = " + id);

        val.clear();

        return id;
    }

    public ArrayList<Integer> getConvertToList(int unitID){
        ArrayList<Integer> toList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, conversion_table, new String[]{"resultID"}, "unitID = ?", new String[]{Integer.toString(unitID)}, null, null, null, null);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                int id = cursor.getInt(cursor.getColumnIndex("resultID"));
                toList.add(id);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return toList;
    }
}