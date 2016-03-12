package com.logmedown.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.logmedown.activity.NoteActivity;
import com.logmedown.adapter.NoteRecyclerAdapter;
import com.example.arces.logmedown.R;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView recyclerView;

    private User user;

    private Animation slideDown;
    private Animation slideUp;
    private Animation zoomIn;
    private Animation zoomOut;

    private FloatingActionButton homeAddNoteFab;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_home, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        zoomIn = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);

        homeAddNoteFab = (FloatingActionButton)view.findViewById(R.id.homeAddNoteFab);

        homeAddNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeAddNoteFab.startAnimation(zoomIn);

                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra("logged_user", user);
                intent.putExtra("note_action", "add");
                startActivityForResult(intent, 2);
            }
        });

        noteRecyclerAdapter = new NoteRecyclerAdapter(user.getNotes());

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_home);
        recyclerView.setAdapter(noteRecyclerAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        homeAddNoteFab.startAnimation(zoomOut);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //homeAddNoteFab.startAnimation(zoomOut);

        switch(resultCode){
            case 2:
                Log.d("result_home_test", "SUCCESS");
                Note editedNote = (Note) data.getSerializableExtra("saved_note");

                Log.d("result_home_test", editedNote.getTitle());

            default:
                Log.d("result_test_home", "FAIL");
                break;
        }
    }
}
