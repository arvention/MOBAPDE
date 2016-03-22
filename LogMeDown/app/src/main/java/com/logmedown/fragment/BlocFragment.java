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
import com.logmedown.adapter.BlocRecyclerAdapter;
import com.logmedown.model.User;

public class BlocFragment extends Fragment {

    private User user;
    private FloatingActionButton blocAddBlocFab;
    private RecyclerView recyclerView;
    private BlocRecyclerAdapter blocRecyclerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_bloc, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        blocAddBlocFab = (FloatingActionButton) view.findViewById(R.id.add_bloc_fab);
        blocAddBlocFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BlocAddActivity.class);
                intent.putExtra("logged_user", user);
                startActivityForResult(intent, 3);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.bloc_recycler_view);
        blocRecyclerAdapter = new BlocRecyclerAdapter(user.getBlocs(), getActivity(), user, recyclerView);
        recyclerView.setAdapter(blocRecyclerAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public FloatingActionButton getBlocAddBlocFab(){
        return blocAddBlocFab;
    }

    public BlocRecyclerAdapter getBlocRecyclerAdapter(){ return blocRecyclerAdapter; }
}
