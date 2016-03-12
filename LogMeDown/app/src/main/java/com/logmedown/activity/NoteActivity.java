package com.logmedown.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.arces.logmedown.R;
import com.logmedown.database.Database;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    private ImageButton saveBtn, discardBtn;
    private EditText editTitle, editContent;
    private User loggedUser;
    private Database db;
    private String action;
    private Note editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //retrieve note action
        action = getIntent().getStringExtra("note_action");
        Log.d("note_debug", action);


        //retrieve logged user details
        loggedUser = (User) getIntent().getSerializableExtra("logged_user");

        //database
        db = Database.getInstance(this);

        //UI components
        saveBtn = (ImageButton) findViewById(R.id.noteSaveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        discardBtn = (ImageButton) findViewById(R.id.noteDiscardButton);
        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNoteAction();
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

    public void cancelNoteAction() {
        if(action.equals("add")) {
            new AlertDialog.Builder(this)
                    .setTitle("Discard Note")
                    .setMessage(R.string.textSaveDiscardPrompt)
                    .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.textLogOutNo, null)
                    .show();
        }
        else if(action.equals("edit")){
            new AlertDialog.Builder(this)
                    .setTitle("Discard Changes")
                    .setMessage(R.string.textEditDiscardPrompt)
                    .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.textLogOutNo, null)
                    .show();
        }
    }

    public void saveNote() {
        if (action.equals("add")) {
            new AlertDialog.Builder(this)
                    .setTitle("Save Note")
                    .setMessage(R.string.textSavePrompt)
                    .setPositiveButton(R.string.textSave, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Note note = new Note();

                            note.setCreator(loggedUser);
                            note.setTitle(editTitle.getText().toString());
                            note.setContent(editContent.getText().toString());
                            note.setDate(new Date());

                            Log.d("add_note", note.getTitle() + " " + note.getContent());
                            db.addNote(note);

                            Intent intent = new Intent();
                            intent.putExtra("saved_note", note);

                            setResult(2, intent);
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.textCancel, null)
                    .show();
        }
        else if(action.equals("edit")){
            new AlertDialog.Builder(this)
                    .setTitle("Edit Note")
                    .setMessage(R.string.textEditSavePrompt)
                    .setPositiveButton(R.string.textSave, new DialogInterface.OnClickListener() {
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
                    .setNegativeButton(R.string.textCancel, null)
                    .show();
        }
    }
}
