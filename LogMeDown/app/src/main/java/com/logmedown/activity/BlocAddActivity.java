package com.logmedown.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.arces.logmedown.R;
import com.logmedown.adapter.AddBlocMemberRecyclerAdapter;
import com.logmedown.model.User;

public class BlocAddActivity extends AppCompatActivity {

    private User user;

    private ImageButton discardButton;
    private EditText blocName;
    private ImageButton saveButton;
    private RecyclerView recyclerView;
    private AddBlocMemberRecyclerAdapter addBlocMemberRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_add);

        user = (User) getIntent().getSerializableExtra("logged_user");

        // temporary
        User friend = new User();
        friend.setFirstName("Nog");
        friend.setLastName("Nog");
        friend.setUsername("noggie");
        for(int i = 0; i < 10; i++) {
            user.getFriends().add(friend);
        }

        this.discardButton = (ImageButton)findViewById(R.id.addBlocDiscardButton);
        this.blocName = (EditText)findViewById(R.id.addBlocName);
        this.saveButton = (ImageButton)findViewById(R.id.addBlocSaveButton);

        this.recyclerView = (RecyclerView)findViewById(R.id.recycler_view_add_bloc);
        recyclerView.setHasFixedSize(true);
        addBlocMemberRecyclerAdapter = new AddBlocMemberRecyclerAdapter(user.getFriends(), this, recyclerView);
        recyclerView.setAdapter(addBlocMemberRecyclerAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
}
