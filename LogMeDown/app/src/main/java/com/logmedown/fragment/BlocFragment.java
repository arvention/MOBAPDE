package com.logmedown.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arces.logmedown.R;
import com.logmedown.activity.BlocAddActivity;
import com.logmedown.activity.NoteActivity;
import com.logmedown.model.User;

public class BlocFragment extends Fragment {

    private User user;
    private FloatingActionButton blocAddBlocFab;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_bloc, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("logged_user");

        blocAddBlocFab = (FloatingActionButton) view.findViewById(R.id.addBlocFab);
        blocAddBlocFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BlocAddActivity.class);
                intent.putExtra("logged_user", user);
                startActivity(intent);
                //startActivityForResult(intent, 2);
            }
        });

        return view;
    }

    public FloatingActionButton getBlocAddBlocFab(){
        return blocAddBlocFab;
    }
}
