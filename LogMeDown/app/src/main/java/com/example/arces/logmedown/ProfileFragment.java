package com.example.arces.logmedown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class ProfileFragment extends Fragment {
    private String USERPREFERENCES = "UserPreferences";
    private static final String LOGGEDUSER = "loggedUser";
    private TextView profileName, profileEmail, profileUsername;
    private Button addNoteBtn;
    private SharedPreferences sharedPreferences;
    private RecyclerView rv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_profile, container, false);

        profileName = (TextView) view.findViewById(R.id.profileName);
        profileEmail = (TextView) view.findViewById(R.id.profileEmail);
        profileUsername = (TextView) view.findViewById(R.id.profileUsername);
        addNoteBtn = (Button) view.findViewById(R.id.addNoteBtn);

        addNoteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNote.class));
            }
        });

        sharedPreferences = this.getActivity().getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOGGEDUSER, "");
        User user = gson.fromJson(json, User.class);

        profileName.setText(user.getFirstName() + " " + user.getLastName());
        profileEmail.setText(user.getEmailAddress());
        profileUsername.setText(user.getUsername());

        createNotesView(view);
        return view;
    }

    public void createNotesView(View view){
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
    }
}
