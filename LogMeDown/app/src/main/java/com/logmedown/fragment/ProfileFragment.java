package com.logmedown.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logmedown.activity.NoteActivity;
import com.logmedown.adapter.NoteRecyclerProfileAdapter;
import com.example.arces.logmedown.R;
import com.logmedown.model.User;

public class ProfileFragment extends Fragment {
    private TextView profileName, profileEmail, profileUsername;
    private ImageView profileImage;

    private User loggedUser;
    private LinearLayout actionMenu;
    private ImageButton editNoteButton;
    private ImageButton viewNoteButton;
    private ImageButton deleteNoteButton;

    private NoteRecyclerProfileAdapter noteRecyclerProfileAdapter;
    private RecyclerView recyclerView;

    private Animation zoomIn;
    private Animation zoomOut;

    private FloatingActionButton profileAddNoteFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        zoomIn = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);

        profileAddNoteFab = (FloatingActionButton)view.findViewById(R.id.profile_add_note_fab);

        profileImage = (ImageView) view.findViewById(R.id.profile_image);

        profileName = (TextView) view.findViewById(R.id.profile_name);
        profileEmail = (TextView) view.findViewById(R.id.profile_email);
        profileUsername = (TextView) view.findViewById(R.id.profile_username);
        actionMenu = (LinearLayout) view.findViewById(R.id.profile_note_action_menu);

        editNoteButton = (ImageButton) view.findViewById(R.id.profile_note_edit_button);
        viewNoteButton = (ImageButton) view.findViewById(R.id.profile_note_view_button);
        deleteNoteButton = (ImageButton) view.findViewById(R.id.profile_note_delete_button);

        loggedUser = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        profileImage.setImageResource(R.drawable.profile_img);

        profileName.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName());
        profileEmail.setText(loggedUser.getEmailAddress());
        profileUsername.setText(loggedUser.getUsername());

        recyclerView = (RecyclerView)view.findViewById(R.id.profile_recycler_view);
        recyclerView.setHasFixedSize(true);
        noteRecyclerProfileAdapter = new NoteRecyclerProfileAdapter(loggedUser.getNotes(), getActivity(), actionMenu, editNoteButton, viewNoteButton, deleteNoteButton, recyclerView);
        recyclerView.setAdapter(getNoteRecyclerAdapter());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getProfileAddNoteFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra("logged_user", loggedUser);
                intent.putExtra("note_action", "add");
                startActivityForResult(intent, 2);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getProfileAddNoteFab().startAnimation(zoomOut);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 1:
                Log.d("result_test", "SUCCESS");
                Note editedNote = (Note) data.getSerializableExtra("edited_note");

                int position = data.getIntExtra("position", 0);
                loggedUser.getNoteAt(position).setContent(editedNote.getContent());
                loggedUser.getNoteAt(position).setTitle(editedNote.getTitle());

                getNoteRecyclerAdapter().notifyItemChanged(position);
                break;
            default:
                Log.d("result_test", "FAIL");
                break;
        }
    }*/

    /*public void addNoteToUser(Note note) {
        Log.d("profile_add_debug", "hehe");
        if (loggedUser != null) {
            loggedUser.addNote(note);
            getNoteRecyclerAdapter().notifyDataSetChanged();
        }
    }*/

    public FloatingActionButton getProfileAddNoteFab() {
        return profileAddNoteFab;
    }

    public NoteRecyclerProfileAdapter getNoteRecyclerAdapter() {
        return noteRecyclerProfileAdapter;
    }

    /*public void editNote() {
        Intent intent = new Intent(this.getActivity(), NoteActivity.class);
        intent.putExtra("note_action", "edit");
        intent.putExtra("note_details", loggedUser.getNoteAt(selectedPos));
        intent.putExtra("position", selectedPos);

        startActivityForResult(intent, 1);
    }*/

}
