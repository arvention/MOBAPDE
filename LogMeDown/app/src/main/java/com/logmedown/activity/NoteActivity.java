package com.logmedown.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arces.logmedown.R;
import com.logmedown.database.Database;
import com.logmedown.model.Bloc;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    private ImageButton saveBtn, discardBtn;
    private EditText editTitle, editContent;
    private User loggedUser;
    private Database db;
    private String action;
    private Note editNote;
    private ImageButton locationBtn;
    private Bloc bloc;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //initialize bloc to null (public)
        bloc = null;

        //retrieve note action
        action = getIntent().getStringExtra("note_action");
        Log.d("note_debug", action);

        //retrieve logged user details
        loggedUser = (User) getIntent().getSerializableExtra("logged_user");

        //database
        db = Database.getInstance(this);

        //UI components
        saveBtn = (ImageButton) findViewById(R.id.note_save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        discardBtn = (ImageButton) findViewById(R.id.note_discard_button);
        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNoteAction();
            }
        });

        editTitle = (EditText) findViewById(R.id.add_note_title);
        editContent = (EditText) findViewById(R.id.add_note_content);

        locationBtn = (ImageButton) findViewById(R.id.note_location_button);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationAlert(action);
            }
        });

        if (action.equals("edit")) {
            editNote = (Note) getIntent().getSerializableExtra("note_details");
            bloc = editNote.getBloc();
            editTitle.setText(editNote.getTitle());
            editContent.setText(editNote.getContent());
        }
    }

    public void cancelNoteAction() {
        if (action.equals("add")) {
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
        } else if (action.equals("edit")) {
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

                            note.setNoteID(db.getLastNoteId()+1);
                            Log.d("getNoteIdOfAdded", String.valueOf(note.getNoteID()));
                            note.setCreator(loggedUser);
                            note.setTitle(editTitle.getText().toString());
                            note.setContent(editContent.getText().toString());
                            note.setDate(new Date());

                            if(index != -1)
                                note.setBloc(loggedUser.getBlocs().get(index));
                            else
                                note.setBloc(null);

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
        } else if (action.equals("edit")) {
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
                            intent.putExtra("edited_note",note);
                            intent.putExtra("position", position);
                            setResult(1, intent);
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.textCancel, null)
                    .show();
        }
    }

    public void showLocationAlert(final String action) {
        //ArrayList<Bloc> blocList = loggedUser.getBlocs();
        ArrayList<String> blocList = new ArrayList<>();

        //add public to list of choices
        blocList.add(0, "Public");
        for(int i = 0; i < loggedUser.getBlocs().size(); i++){
            blocList.add(loggedUser.getBlocs().get(i).getName());
        }

        //alert
        AlertDialog.Builder locationAlert = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView = inflater.inflate(R.layout.note_location_alert, null);

        //spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, blocList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner blocSpinner = (Spinner) alertView.findViewById(R.id.bloc_spinner);
        blocSpinner.setAdapter(adapter);

        locationAlert.setView(alertView);
        locationAlert.setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String location = blocSpinner.getSelectedItem().toString();
                if (location.equals("Public")) {
                    locationBtn.setImageResource(R.drawable.public_note_icon);
                    bloc = null;
                } else {
                    index = -1;
                    for(int i = 0; i < loggedUser.getBlocs().size() && index == -1; i++){
                        if(loggedUser.getBlocs().get(i).getName().equals(location))
                            index = i;
                    }
                }
            }
        });

        locationAlert.setNegativeButton(R.string.textCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do nothing
            }
        });

        locationAlert.show();
    }
}
