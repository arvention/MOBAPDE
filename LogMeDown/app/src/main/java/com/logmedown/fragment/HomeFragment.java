package com.logmedown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logmedown.adapter.NoteRecyclerAdapter;
import com.example.arces.logmedown.R;
import com.logmedown.model.User;

public class HomeFragment extends Fragment {

    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView recyclerView;
    private User user;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_home, container, false);
        user = (User) getActivity().getIntent().getSerializableExtra("logged_user");
        noteRecyclerAdapter = new NoteRecyclerAdapter(user.getNotes());

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_home);
        recyclerView.setAdapter(noteRecyclerAdapter);

        return view;
    }
}
