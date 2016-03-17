package com.logmedown.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logmedown.adapter.NoteRecyclerAdapter;
import com.example.arces.logmedown.R;
import com.logmedown.model.User;

public class ProfileFragment extends Fragment {
    private TextView profileName, profileEmail, profileUsername;
    private ImageView profileImage;

    private User user;
    private LinearLayout actionMenu;
    private ImageButton editNoteButton;
    private ImageButton viewNoteButton;
    private ImageButton deleteNoteButton;

    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView recyclerView;

    private FloatingActionButton profileAddNoteFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        profileAddNoteFab = (FloatingActionButton)view.findViewById(R.id.profileAddNoteFab);

        profileImage = (ImageView) view.findViewById(R.id.profileImage);

        profileName = (TextView) view.findViewById(R.id.profileName);
        profileEmail = (TextView) view.findViewById(R.id.profileEmail);
        profileUsername = (TextView) view.findViewById(R.id.profileUsername);
        actionMenu = (LinearLayout) view.findViewById(R.id.profileNoteActionMenu);

        editNoteButton = (ImageButton) view.findViewById(R.id.profileNoteEditButton);
        viewNoteButton = (ImageButton) view.findViewById(R.id.profileNoteViewButton);
        deleteNoteButton = (ImageButton) view.findViewById(R.id.profileNoteDeleteButton);

        user = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        profileImage.setImageResource(R.drawable.profile_img);

        profileName.setText(user.getFirstName() + " " + user.getLastName());
        profileEmail.setText(user.getEmailAddress());
        profileUsername.setText(user.getUsername());

        noteRecyclerAdapter = new NoteRecyclerAdapter(user.getNotes(), getActivity(), actionMenu, editNoteButton, viewNoteButton, deleteNoteButton);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_profile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(getNoteRecyclerAdapter());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 1:
                Log.d("result_test", "SUCCESS");
                Note editedNote = (Note) data.getSerializableExtra("edited_note");

                int position = data.getIntExtra("position", 0);
                user.getNoteAt(position).setContent(editedNote.getContent());
                user.getNoteAt(position).setTitle(editedNote.getTitle());

                getNoteRecyclerAdapter().notifyItemChanged(position);
                break;
            default:
                Log.d("result_test", "FAIL");
                break;
        }
    }*/

    /*public void addNoteToUser(Note note) {
        Log.d("profile_add_debug", "hehe");
        if (user != null) {
            user.addNote(note);
            getNoteRecyclerAdapter().notifyDataSetChanged();
        }
    }*/

    public FloatingActionButton getProfileAddNoteFab() {
        return profileAddNoteFab;
    }

    public NoteRecyclerAdapter getNoteRecyclerAdapter() {
        return noteRecyclerAdapter;
    }

    /*public void editNote() {
        Intent intent = new Intent(this.getActivity(), NoteActivity.class);
        intent.putExtra("note_action", "edit");
        intent.putExtra("note_details", user.getNoteAt(selectedPos));
        intent.putExtra("position", selectedPos);

        startActivityForResult(intent, 1);
    }*/

}
