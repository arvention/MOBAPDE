package com.example.arces.mini_challenge_2_unitconversion;

import android.content.Context;
import android.util.Log;

import com.example.arces.mini_challenge_2_unitconversion.Database.Database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Arces on 16/03/2016.
 */
public class Converter {
    private Database db;

    public Converter(Context context) {
        db = Database.getInstance(context);
    }

    public float tryConvert(int fromID, float value, int toID) {
        float result;
        if (fromID != toID) {
            result = chainConvert(fromID, value, toID);
        } else {
            result = value;
        }

        return result;
    }

    public float convert(int fromID, float value, int toID, String multiplier) {
        float result = 0;
        Log.d("debug", "value = " + value + " from = " + fromID + " to = " + toID);
        if (canParse(multiplier)) {
            float x = Float.parseFloat(multiplier);
            result = value * x;
        } else {
            if (fromID == 13) {
                result = convertCelsius(value);
            } else if (fromID == 14) {
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

    public float convertCelsius(float value) {
        float f = (float) (value * 1.8 + 32);

        return f;
    }

    public float convertFahrenheit(float value) {
        float c = (float) ((value - 32) * 1.8);

        return c;
    }

    public float chainConvert(int fromID, float value, int toID) {
        ArrayList<Integer> idList;
        float result;

        idList = getConversionPath(fromID, toID, fromID);

        float temp = value;
        for (int i = 0; i < idList.size() - 1; i++) {
            Log.d("debug_chain", "id1 = " + idList.get(i) + " id2 = " + idList.get(i + 1));
            String multiplier = db.getMultiplier(idList.get(i), idList.get(i + 1));
            temp = convert(idList.get(i), temp, idList.get(i + 1), multiplier);
        }

        result = temp;

        return result;
    }

    public ArrayList<Integer> getConversionPath(int fromID, int toID, int parentID) {
        ArrayList<Integer> idPath = new ArrayList<>();
        ArrayList<Integer> toList = db.getConvertToList(fromID);
        Log.d("debug_path", "" + fromID);

        if(toList.contains(parentID)){
            toList.remove(new Integer(parentID));
        }
        idPath.add(fromID);

        if (toList.contains(toID)) {
            idPath.add(toID);
        } else {
            ArrayList<Integer> tempPath = new ArrayList<>();
            for (int i = 0; i < toList.size() && tempPath.isEmpty(); i++) {
                tempPath = getConversionPath(toList.get(i), toID, fromID);
            }

            if (!tempPath.isEmpty()) {
                idPath.removeAll(tempPath);
                idPath.addAll(tempPath);
            } else {
                idPath.remove(new Integer(fromID));
            }
        }

        return idPath;
    }
}
