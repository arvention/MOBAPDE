package com.example.arces.introapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LannisterActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lannister);

        listView = (ListView) findViewById(R.id.lannister_list);

        String[] labels = new String[]{"Name", "Birthdate", "House", "Hobbies", "Interest", "2016 New Year's Resolution"};
        final String[] values = new String[]{"Arces Talavera", "Dec. 28 1996", "Lannister", "Eating\nSleeping\nPlaying Video Games\nWatching TV Series", "Game of Thrones\nPokemon\nStar Wars\nYummy Foods", "Stop Procrastinating"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, labels);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemVal = values[itemPosition];

                Toast.makeText(getApplicationContext(), (String)listView.getItemAtPosition(itemPosition) + "\n---------\n"+itemVal, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goBack(View view){
        this.finish();
    }
}
