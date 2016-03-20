package com.logmedown.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.model.Note;

public class NoteViewActivity extends AppCompatActivity {

    private Note note;
    private TextView noteName;
    private ImageView userImage;
    private TextView userName;
    private ImageButton noteImg;
    private TextView noteImgMore;
    private TextView noteContents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        note = (Note)getIntent().getSerializableExtra("note");

        noteName = (TextView)findViewById(R.id.noteViewNoteName);
        userImage = (ImageView)findViewById(R.id.noteViewNoteUserImg);
        userName = (TextView)findViewById(R.id.noteViewUserName);
        noteImg = (ImageButton)findViewById(R.id.noteViewImg);
        noteImgMore = (TextView)findViewById(R.id.noteViewImgMore);
        noteContents = (TextView)findViewById(R.id.noteViewContents);

        noteName.setText(note.getTitle());
        // reminder set image for user
        userName.setText(note.getCreator().getFirstName() + " " + note.getCreator().getLastName());
        // reminder set image for note
        // reminder set text for img more
        noteContents.setText(note.getContent());
    }
}
