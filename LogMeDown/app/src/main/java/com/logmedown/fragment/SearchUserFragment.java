package com.logmedown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arces.logmedown.R;
import com.logmedown.adapter.SearchUserRecyclerAdapter;
import com.logmedown.model.User;

import java.util.ArrayList;

public class SearchUserFragment extends Fragment {
    private ArrayList<User> users;

    private SearchUserRecyclerAdapter sura;
    private RecyclerView recyclerView;
    private User loggedUser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_search_user, container, false);

        loggedUser = (User) getActivity().getIntent().getSerializableExtra("logged_user");
        recyclerView = (RecyclerView)view.findViewById(R.id.search_user_recycler);
        recyclerView.setHasFixedSize(true);
        sura = new SearchUserRecyclerAdapter(getActivity(), users, loggedUser);
        recyclerView.setAdapter(sura);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public void updateList(ArrayList<User> users){
        this.users = users;
        if(sura != null){
            sura.updateList(users);
        }
    }
}