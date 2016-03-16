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
            if(fromID == 13){
                result = convertCelsius(value);
            } else if(fromID == 14){
                result = convertFahrenheit(value);
            }
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

    public float convertCelsius(float value){
        float f = (float)(value * 1.8 + 32);

        return f;
    }

    public float convertFahrenheit(float value){
        float c = (float)((value - 32) * 1.8);

        return c;
    }
}
