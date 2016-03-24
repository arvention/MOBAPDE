package com.logmedown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arces.logmedown.R;
import com.logmedown.adapter.NoteRecyclerHomeAdapter;
import com.logmedown.adapter.NoteRecyclerProfileAdapter;
import com.logmedown.adapter.SearchBlocRecyclerAdapter;
import com.logmedown.model.Bloc;
import com.logmedown.model.User;

import java.util.ArrayList;

public class SearchBlocFragment extends Fragment {
    private ArrayList<Bloc> blocs;

    private SearchBlocRecyclerAdapter sbra;
    private RecyclerView recyclerView;
    private User loggedUser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_search_bloc, container, false);

        loggedUser = (User) getActivity().getIntent().getSerializableExtra("logged_user");
        recyclerView = (RecyclerView)view.findViewById(R.id.search_bloc_recycler);
        recyclerView.setHasFixedSize(true);
        sbra = new SearchBlocRecyclerAdapter(blocs, getActivity(), loggedUser);
        recyclerView.setAdapter(sbra);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void updateList(ArrayList<Bloc> blocs){
        this.blocs = blocs;
        if(sbra != null){
            sbra.updateList(blocs);
        }
    }
}
