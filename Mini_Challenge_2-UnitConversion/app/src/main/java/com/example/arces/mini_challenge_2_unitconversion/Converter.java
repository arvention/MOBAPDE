package com.example.arces.mini_challenge_2_unitconversion;

import android.content.Context;
import android.util.Log;

import com.example.arces.mini_challenge_2_unitconversion.Database.Database;

/**
 * Created by Arces on 16/03/2016.
 */
public class Converter {
    private Context context;
    private Database db;

    public Converter(Context context) {
        db = Database.getInstance(context);
    }

    public float convert(int fromID, float value, int toID) {
        float result = 0;
        Log.d("debug", "value = " + value + " from = " + fromID + " to = " + toID);
        String multiplier = db.getMultiplier(fromID, toID);
        if (fromID == toID) {
            result = value;
        } else if (canParse(multiplier)) {
            float x = Float.parseFloat(multiplier);
            result = value * x;
        } else {
            //Unit unit = db.getUnit(fromID);
            //multiplier.replace(unit.getName(), Float.toString(value));
        }
        return result;
    }

    public boolean canParse(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
