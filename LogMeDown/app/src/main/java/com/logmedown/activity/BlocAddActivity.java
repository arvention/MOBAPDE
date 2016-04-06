package com.logmedown.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.arces.logmedown.R;
import com.logmedown.adapter.AddBlocMemberRecyclerAdapter;
import com.logmedown.database.Database;
import com.logmedown.model.Bloc;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.util.ArrayList;

public class BlocAddActivity extends AppCompatActivity {

    private User user;
    private Database db;

    private EditText blocName;
    private ImageButton saveButton;
    private Spinner blocType;
    private RecyclerView recyclerView;
    private AddBlocMemberRecyclerAdapter addBlocMemberRecyclerAdapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_add);

        user = (User) getIntent().getSerializableExtra("logged_user");
        db = Database.getInstance(this);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.add_bloc_header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.blocName = (EditText)findViewById(R.id.add_bloc_name);
        this.saveButton = (ImageButton)findViewById(R.id.add_bloc_save_button);

        this.blocType = (Spinner)findViewById(R.id.add_bloc_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Public", "Closed", "Private"});
        this.blocType.setAdapter(adapter);

        this.recyclerView = (RecyclerView)findViewById(R.id.add_bloc_recycler_view);
        recyclerView.setHasFixedSize(true);
        addBlocMemberRecyclerAdapter = new AddBlocMemberRecyclerAdapter(user.getFriends(), this, recyclerView);
        recyclerView.setAdapter(addBlocMemberRecyclerAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAction();
            }
        });
    }

    public void discardAction(){
        new AlertDialog.Builder(this)
                .setTitle("Discard Bloc")
                .setMessage(R.string.textDiscardBlocPrompt)
                .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(R.string.textLogOutNo, null)
                .show();
    }

    public void saveAction(){
        new AlertDialog.Builder(this)
                .setTitle("Save Bloc")
                .setMessage(R.string.textSaveBlocPrompt)
                .setPositiveButton(R.string.textSave, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bloc bloc = new Bloc();

                        bloc.setCreator(user);
                        bloc.setName(blocName.getText().toString());
                        bloc.setType(blocType.getSelectedItem().toString());

                        ArrayList<User> members = new ArrayList<>();
                        ArrayList<Note> notes = new ArrayList<>();

                        AddBlocMemberRecyclerAdapter addBlocMemberRecyclerAdapter = (AddBlocMemberRecyclerAdapter)recyclerView.getAdapter();

                        for (int i = 0; i < addBlocMemberRecyclerAdapter.getSelectedFriends().size(); i++) {
                            members.add(addBlocMemberRecyclerAdapter.getSelectedFriends().get(i));
                        }
                        bloc.setMembers(members);
                        bloc.setNotes(notes);
                        
                        db.addBloc(bloc);

                        Intent intent = new Intent();
                        intent.putExtra("saved_bloc", bloc);
                        setResult(3, intent);
                        finish();
                    }

                })
                .setNegativeButton(R.string.textCancel, null)
                .show();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            discardAction();
        }

        return super.onOptionsItemSelected(item);
    }
}
