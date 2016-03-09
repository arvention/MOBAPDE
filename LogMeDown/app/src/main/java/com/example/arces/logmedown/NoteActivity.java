package com.example.arces.logmedown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

public class NoteActivity extends AppCompatActivity {
    private Button saveBtn, cancelBtn;
    private EditText editTitle, editContent;
    private User loggedUser;
    private Database db;
    private String action;
    private Note editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //retrieve note action
        action = getIntent().getStringExtra("note_action");
        Log.d("note_debug", action);


        //retrieve logged user details
        loggedUser = (User) getIntent().getSerializableExtra("logged_user");

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
            editNote = (Note) getIntent().getSerializableExtra("note_details");

            editTitle.setText(editNote.getTitle());
            editContent.setText(editNote.getContent());
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
        if (action.equals("add")) {
            new AlertDialog.Builder(this)
                    .setTitle("Save Note")
                    .setMessage(R.string.textSavePrompt)
                    .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Note note = new Note();

                            note.setCreator(loggedUser);
                            note.setTitle(editTitle.getText().toString());
                            note.setContent(editContent.getText().toString());

                            Log.d("add_note", note.getTitle() + " " + note.getContent());
                            db.addNote(note);
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.textLogOutNo, null)
                    .show();
        }
        else if(action.equals("edit")){
            new AlertDialog.Builder(this)
                    .setTitle("Edit Note")
                    .setMessage(R.string.textEditSavePrompt)
                    .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Note note = new Note();

                            note.setNoteID(editNote.getNoteID());
                            note.setTitle(editTitle.getText().toString());
                            note.setContent(editContent.getText().toString());

                            db.editNote(note);

                            int position = getIntent().getIntExtra("position", 0);
                            Intent intent = new Intent();
                            intent.putExtra("edited_note", note);
                            intent.putExtra("position", position);
                            setResult(1, intent);
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.textLogOutNo, null)
                    .show();
        }
    }
}
