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

public class NoteActivity extends AppCompatActivity {
    private static final String USERPREFERENCES = "UserPreferences";
    private static final String NOTEPREFERENCES = "NotePreferences";
    private static final String LOGGEDUSER = "loggedUser";
    private Button saveBtn, cancelBtn;
    private SharedPreferences sharedPreferences;
    private EditText editTitle, editContent;
    private User loggedUser;
    private Database db;
    private String action;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //retrieve note action
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            action = extras.getString("note_action");
        }

        //retrieve logged user details
        gson = new Gson();
        sharedPreferences = getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(LOGGEDUSER, "");
        loggedUser = gson.fromJson(json, User.class);

        //database
        db = Database.getInstance(this);

        //UI components
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

        if(action.equals("edit")){
            Note note = (Note) getIntent().getSerializableExtra("note_details");

            editTitle.setText(note.getTitle());
            editContent.setText(note.getContent());
        }
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

    public void saveNote() {
        if (action == "add") {
            Note note = new Note();

            note.setCreator(loggedUser);
            note.setTitle(editTitle.getText().toString());
            note.setContent(editContent.getText().toString());

            db.addNote(note);
        }
        else if(action == "edit"){

        }
    }
}
