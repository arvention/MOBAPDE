package com.example.arces.logmedown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements NoteListRecyclerAdapter.OnItemClickListener{
    private TextView profileName, profileEmail, profileUsername;
    private NoteListRecyclerAdapter noteAdapter;
    private RecyclerView rv;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        profileName = (TextView) view.findViewById(R.id.profileName);
        profileEmail = (TextView) view.findViewById(R.id.profileEmail);
        profileUsername = (TextView) view.findViewById(R.id.profileUsername);

        user = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        profileName.setText(user.getFirstName() + " " + user.getLastName());
        profileEmail.setText(user.getEmailAddress());
        profileUsername.setText(user.getUsername());

        createNotesView(view);

        this.noteAdapter = new NoteListRecyclerAdapter(this, container.getContext(), user.getNotes());
        this.rv.setAdapter(noteAdapter);

        return view;
    }

    public void createNotesView(View view){
        rv = (RecyclerView) view.findViewById(R.id.profileNoteRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onItemClick(int position, Note note) {

        Log.d("debug_recycler", note.getTitle());

        /*
        Intent intent = new Intent(this.getActivity(), NoteActivity.class);
        intent.putExtra("note_action", "edit");
        intent.putExtra("note_details", note);
        intent.putExtra("position", position);

        startActivityForResult(intent, 1);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode){
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

    public void addNoteToUser(Note note){
        Log.d("profile_add_debug", "hehe");
        if(user != null) {
            user.addNote(note);
            noteAdapter.notifyDataSetChanged();
        }
    }
}
