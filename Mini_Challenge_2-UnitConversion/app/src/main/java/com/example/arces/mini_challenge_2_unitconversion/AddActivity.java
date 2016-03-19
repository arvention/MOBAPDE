package com.example.arces.mini_challenge_2_unitconversion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arces.mini_challenge_2_unitconversion.Database.Database;

import java.io.Serializable;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    private String[] unitCategory;
    private Spinner spinnerCategory;
    private Spinner spinnerFromUnit;
    private EditText editTextToValue;
    private EditText editTextToUnit;

    private Database db;
    private Button buttonAdd;
    private Button buttonCancel;
    private ArrayList<Unit> unitList;
    private int selectedUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.db = Database.getInstance(this);
        this.unitCategory = new String[]{"Distance", "Weight", "Volume", "Area", "Temperature"};
        this.unitList = new ArrayList<>();

        spinnerCategory = (Spinner) findViewById(R.id.addcategories);
        spinnerFromUnit = (Spinner) findViewById(R.id.addfromUnit);
        editTextToValue = (EditText) findViewById(R.id.addtoValue);
        editTextToUnit = (EditText) findViewById(R.id.addtoUnitName);

        buttonAdd = (Button) findViewById(R.id.addButton);
        buttonCancel = (Button) findViewById(R.id.cancelButton);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = spinnerCategory.getSelectedItem().toString();
                String toValue = editTextToValue.getText().toString();
                String toUnit = editTextToUnit.getText().toString();

                Unit unit = new Unit(toUnit, category);
                Unit existingUnit = unitList.get(selectedUnit);
                Intent newUnit = new Intent();
                newUnit.putExtra("new_unit", unit);
                long newID = db.addUnit(existingUnit,toValue,unit);

                unit.setUnitID((int)newID);
                setResult(1, newUnit);

                Toast.makeText(getApplicationContext(), unit.getName() + " has been added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitCategory);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = spinnerCategory.getSelectedItem().toString().toLowerCase();
                unitList = db.getUnits(category);
                updateSpinners();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFromUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUnit = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void updateSpinners(){
        ArrayList<String> nameList = new ArrayList<>();
        for(Unit unit: unitList){
            nameList.add(unit.getName());
        }

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nameList);
        spinnerFromUnit.setAdapter(fromAdapter);
    }
}
