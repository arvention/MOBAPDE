package com.example.arces.mini_challenge_2_unitconversion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private Spinner spinnerFromUnit;
    private EditText editTextToValue;
    private EditText editTextToUnit;

    private Button buttonAdd;
    private Button buttonCancel;

    //your mom

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        spinnerCategory = (Spinner)findViewById(R.id.addcategories);
        spinnerFromUnit = (Spinner)findViewById(R.id.addfromUnit);
        editTextToValue = (EditText)findViewById(R.id.addtoValue);
        editTextToUnit = (EditText)findViewById(R.id.addtoUnitName);

        buttonAdd = (Button)findViewById(R.id.addButton);
        buttonCancel = (Button)findViewById(R.id.cancelButton);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = spinnerCategory.getSelectedItem().toString();
                String fromUnit = spinnerFromUnit.getSelectedItem().toString();
                float toValue = Float.parseFloat(editTextToValue.getText().toString());
                String toUnit = editTextToValue.getText().toString();

                // add to database

                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
