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

import com.example.arces.logmedown.R;
import com.logmedown.activity.BlocAddActivity;
import com.logmedown.adapter.NoteRecyclerHomeAdapter;
import com.logmedown.adapter.NoteRecyclerProfileAdapter;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.util.ArrayList;

public class SearchNoteFragment extends Fragment {
    private ArrayList<Note> notes;

    private NoteRecyclerProfileAdapter noteRecyclerProfileAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_search_note, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.search_note_recycler);
        recyclerView.setHasFixedSize(true);
        //noteRecyclerHomeAdapter = new NoteRecyclerHomeAdapter(user.getNotes(), getActivity(), actionMenu, editNoteButton, viewNoteButton, deleteNoteButton, recyclerView, user);
        //recyclerView.setAdapter(getNoteRecyclerAdapter());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void updateList(ArrayList<Note> notes){
        this.notes = notes;
    }
}
