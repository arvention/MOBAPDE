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
import android.widget.LinearLayout;

import com.logmedown.activity.NoteActivity;
import com.example.arces.logmedown.R;
import com.logmedown.adapter.NoteRecyclerHomeAdapter;
import com.logmedown.model.User;


public class HomeFragment extends Fragment {

    private NoteRecyclerHomeAdapter noteRecyclerHomeAdapter;
    private RecyclerView recyclerView;

    private User loggedUser;
    private LinearLayout actionMenu;
    private ImageButton editNoteButton;
    private ImageButton viewNoteButton;
    private ImageButton deleteNoteButton;

    private Animation zoomIn;
    private Animation zoomOut;

    private FloatingActionButton homeAddNoteFab;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_home, container, false);

        zoomIn = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);

        loggedUser = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        homeAddNoteFab = (FloatingActionButton)view.findViewById(R.id.home_add_note_fab);

        getHomeAddNoteFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra("logged_user", loggedUser);
                intent.putExtra("note_action", "add");
                startActivityForResult(intent, 2);
            }
        });

        actionMenu = (LinearLayout) view.findViewById(R.id.home_note_action_menu);

        editNoteButton = (ImageButton) view.findViewById(R.id.home_note_edit_button);
        viewNoteButton = (ImageButton) view.findViewById(R.id.home_note_view_button);
        deleteNoteButton = (ImageButton) view.findViewById(R.id.home_note_delete_button);

        recyclerView = (RecyclerView)view.findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);
        noteRecyclerHomeAdapter = new NoteRecyclerHomeAdapter(loggedUser.getNotes(), getActivity(), actionMenu, editNoteButton, viewNoteButton, deleteNoteButton, recyclerView, loggedUser);
        recyclerView.setAdapter(getNoteRecyclerAdapter());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getHomeAddNoteFab().startAnimation(zoomOut);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode){
            case 2:
                Log.d("result_home_test", "SUCCESS");
                Note editedNote = (Note) data.getSerializableExtra("saved_note");

                Log.d("result_home_test", editedNote.getTitle());

            default:
                Log.d("result_test_home", "FAIL");
                break;
        }
    }*/

    public FloatingActionButton getHomeAddNoteFab() {
        return homeAddNoteFab;
    }

    public NoteRecyclerHomeAdapter getNoteRecyclerAdapter() {
        return noteRecyclerHomeAdapter;
    }
}
