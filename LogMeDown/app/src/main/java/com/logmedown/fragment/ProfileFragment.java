package com.logmedown.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logmedown.activity.NoteActivity;
import com.logmedown.database.Database;
import com.logmedown.model.Note;
import com.logmedown.adapter.NoteListRecyclerAdapter;
import com.example.arces.logmedown.R;
import com.logmedown.model.User;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements NoteListRecyclerAdapter.OnItemClickListener {
    private TextView profileName, profileEmail, profileUsername;
    private NoteListRecyclerAdapter noteAdapter;
    private RecyclerView rv;
    private User user;
    private LinearLayout actionMenu;
    private ImageButton editNoteButton, deleteNoteButton;
    private int selectedPos;
    private Database db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        db = Database.getInstance(container.getContext());
        selectedPos = -1;
        profileName = (TextView) view.findViewById(R.id.profileName);
        profileEmail = (TextView) view.findViewById(R.id.profileEmail);
        profileUsername = (TextView) view.findViewById(R.id.profileUsername);
        actionMenu = (LinearLayout) view.findViewById(R.id.noteActionMenu);

        editNoteButton = (ImageButton) view.findViewById(R.id.noteEditButton);
        deleteNoteButton = (ImageButton) view.findViewById(R.id.noteDeleteButton);

        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNote();
            }
        });

        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });

        user = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        profileName.setText(user.getFirstName() + " " + user.getLastName());
        profileEmail.setText(user.getEmailAddress());
        profileUsername.setText(user.getUsername());

        createNotesView(view);

        this.noteAdapter = new NoteListRecyclerAdapter(this, container.getContext(), user.getNotes());
        this.rv.setAdapter(noteAdapter);

        return view;
    }

    public void createNotesView(View view) {
        rv = (RecyclerView) view.findViewById(R.id.profileNoteRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onItemClick(int position) {
        Log.d("debug_recycler", "clicked = " + position + " vs " + selectedPos);

        if (selectedPos == position) {
            actionMenu.animate()
                    .translationY(actionMenu.getHeight())
                    .alpha(0.0f);
            selectedPos = -1;
        } else {
            if (selectedPos == -1) {
                actionMenu.setVisibility(View.VISIBLE);
                actionMenu.setAlpha(0.0f);
                actionMenu.animate()
                        .translationY(0)
                        .alpha(1.0f);
            }
            selectedPos = position;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 1:
                Log.d("result_test", "SUCCESS");
                Note editedNote = (Note) data.getSerializableExtra("edited_note");

                int position = data.getIntExtra("position", 0);
                user.getNoteAt(position).setContent(editedNote.getContent());
                user.getNoteAt(position).setTitle(editedNote.getTitle());

                noteAdapter.notifyItemChanged(position);
                break;
            default:
                Log.d("result_test", "FAIL");
                break;
        }
    }

    public void addNoteToUser(Note note) {
        Log.d("profile_add_debug", "hehe");
        if (user != null) {
            user.addNote(note);
            noteAdapter.notifyDataSetChanged();
        }
    }

    public void editNote() {
        Intent intent = new Intent(this.getActivity(), NoteActivity.class);
        intent.putExtra("note_action", "edit");
        intent.putExtra("note_details", user.getNoteAt(selectedPos));
        intent.putExtra("position", selectedPos);

        startActivityForResult(intent, 1);
    }

    public void deleteNote() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Delete Note")
                .setMessage(R.string.textDeleteNotePrompt)
                .setPositiveButton(R.string.textConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteNote(user.getNoteAt(selectedPos));

                        user.deleteNote(selectedPos);
                        noteAdapter.deleteNoteAt(selectedPos);
                        actionMenu.setVisibility(View.GONE);
                    }

                })
                .setNegativeButton(R.string.textCancel, null)
                .show();
    }
}
