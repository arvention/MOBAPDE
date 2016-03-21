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

        noteName = (TextView)findViewById(R.id.note_view_note_name);
        userImage = (ImageView)findViewById(R.id.note_view_note_user_img);
        userName = (TextView)findViewById(R.id.note_view_user_name);
        noteImg = (ImageButton)findViewById(R.id.note_view_img);
        noteImgMore = (TextView)findViewById(R.id.note_view_img_more);
        noteContents = (TextView)findViewById(R.id.note_view_contents);

        noteName.setText(note.getTitle());
        // reminder set image for user
        userName.setText(note.getCreator().getFirstName() + " " + note.getCreator().getLastName());
        // reminder set image for note
        // reminder set text for img more
        noteContents.setText(note.getContent());
    }
}
