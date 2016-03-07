package com.example.arces.logmedown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

public class AddNote extends AppCompatActivity {
    private static final String USERPREFERENCES = "UserPreferences";
    private static final String LOGGEDUSER = "loggedUser";
    private Button saveBtn, cancelBtn;
    private SharedPreferences sharedPreferences;
    private EditText editTitle, editContent;
    private User loggedUser;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        sharedPreferences = getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOGGEDUSER, "");
        loggedUser = gson.fromJson(json, User.class);

        db = Database.getInstance(this);

        saveBtn = (Button) findViewById(R.id.addNoteSaveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        cancelBtn = (Button) findViewById(R.id.addNoteCancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAddNote();
            }
        });

        editTitle = (EditText) findViewById(R.id.addNoteTitle);
        editContent = (EditText) findViewById(R.id.addNoteContent);
    }

    public void cancelAddNote() {
        new AlertDialog.Builder(this)
                .setTitle("Discard Note")
                .setMessage(R.string.textDiscardPrompt)
                .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(R.string.textLogOutNo, null)
                .show();
    }

    public void saveNote(){
        Note note = new Note();

        note.setCreator(loggedUser);
        note.setTitle(editTitle.getText().toString());
        note.setContent(editContent.getText().toString());

        db.addNote(note);
    }
}
