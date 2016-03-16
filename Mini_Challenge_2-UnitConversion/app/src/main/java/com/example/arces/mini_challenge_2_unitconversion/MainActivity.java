package com.example.arces.mini_challenge_2_unitconversion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.arces.mini_challenge_2_unitconversion.Database.Database;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[] arraySpinner;
    private Spinner categorySpinner, toSpinner, fromSpinner;
    private EditText fromEdit, toEdit;
    private Database db;
    private ArrayList<Unit> unitList;
    private Converter converter;
    private int selectedTo = -1;
    private int selectedFrom = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteDatabase("UnitConversion.db");
        unitList = new ArrayList<>();
        converter = new Converter(this);
        this.arraySpinner = new String[]{"Distance", "Weight", "Volume", "Area", "Temperature"};
        this.db = Database.getInstance(this);
        db.initializeData();
        categorySpinner = (Spinner)findViewById(R.id.categories);
        fromSpinner = (Spinner)findViewById(R.id.fromSpinner);
        toSpinner = (Spinner)findViewById(R.id.toSpinner);

        fromEdit = (EditText)findViewById(R.id.fromValue);
        toEdit = (EditText)findViewById(R.id.toValue);

        fromEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(fromEdit.getText().length() != 0) {
                    Float fromValue = Float.parseFloat(fromEdit.getText().toString());
                    Float toValue = converter.convert(unitList.get(selectedFrom).getUnitID(), fromValue, unitList.get(selectedTo).getUnitID());
                    toEdit.setText(Float.toString(toValue));
                }
                else{
                    toEdit.setText("");
                }
                return false;
            }
        });

        toEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(toEdit.getText().length() != 0) {
                    Float fromValue = Float.parseFloat(toEdit.getText().toString());
                    Float toValue = converter.convert(unitList.get(selectedTo).getUnitID(), fromValue, unitList.get(selectedFrom).getUnitID());
                    fromEdit.setText(Float.toString(toValue));
                }
                else{
                    fromEdit.setText("");
                }
                return false;
            }
        });

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categorySpinner.getSelectedItem().toString().toLowerCase();

                unitList = db.getUnits(category);
                fromEdit.setText("");
                toEdit.setText("");
                updateSpinners();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFrom = position;
                fromEdit.setText("");
                toEdit.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTo = position;
                fromEdit.setText("");
                toEdit.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateSpinners(){
        ArrayList<String> nameList = new ArrayList<>();
        for(Unit unit: unitList){
            nameList.add(unit.getName());
        }

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nameList);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nameList);
        toSpinner.setAdapter(toAdapter);
    }
}