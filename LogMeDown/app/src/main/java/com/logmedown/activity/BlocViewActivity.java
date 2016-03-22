package com.logmedown.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.database.Database;
import com.logmedown.model.Bloc;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import org.w3c.dom.Text;

import java.util.Date;

public class BlocViewActivity extends AppCompatActivity {

    private User user;
    private Bloc bloc;
    private int position;
    private Boolean isMember;
    private Database db;

    private TextView blocName;
    private ImageView userImage;
    private TextView userName;

    private ImageButton joinButton;
    private ImageButton memberButton;
    private ImageButton editButton;
    private ImageButton deleteButton;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_view);

        user = (User) getIntent().getSerializableExtra("user");
        bloc = (Bloc) getIntent().getSerializableExtra("bloc");
        position = (Integer) getIntent().getSerializableExtra("position");
        db = Database.getInstance(this);
        
        blocName = (TextView) findViewById(R.id.bloc_view_bloc_name);
        userImage = (ImageView) findViewById(R.id.bloc_view_bloc_user_img);
        userName = (TextView) findViewById(R.id.bloc_view_user_name);

        joinButton = (ImageButton) findViewById(R.id.bloc_view_join_button);
        memberButton = (ImageButton) findViewById(R.id.bloc_view_member_button);
        editButton = (ImageButton) findViewById(R.id.bloc_view_edit_button);
        deleteButton = (ImageButton) findViewById(R.id.bloc_view_delete_button);

        recyclerView = (RecyclerView) findViewById(R.id.bloc_view_recycler_view);

        blocName.setText(bloc.getName());
        //set user image later
        userName.setText(user.getFirstName() + " " + user.getLastName());

        if(bloc.getCreator().getUserID() == user.getUserID()){
            joinButton.setVisibility(View.GONE);

            memberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberButtonOnClick();
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editButtonOnClick();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteButtonOnClick();
                }
            });
        }
        else{
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);

            isMember = false;
            for(int i = 0; i < bloc.getMembers().size() && !isMember; i++){
                if(bloc.getMembers().get(i).getUserID() == user.getUserID()){
                    isMember = true;
                }
            }

            if(isMember){
                joinButton.setImageResource(R.drawable.join_joined_bloc_icon);
            }

            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinButtonOnClick();
                }
            });

            memberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberButtonOnClick();
                }
            });
        }
    }

    private void joinButtonOnClick(){

    }

    private void memberButtonOnClick(){

    }

    private void editButtonOnClick(){

    }

    private void deleteButtonOnClick(){
        new AlertDialog.Builder(this)
                .setTitle("Delete Bloc")
                .setMessage(R.string.textDeleteBlocPrompt)
                .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteBloc(bloc);
                        Intent intent = new Intent();
                        intent.putExtra("action", "delete");
                        intent.putExtra("position", position);
                        setResult(4, intent);
                        finish();
                    }

                })
                .setNegativeButton(R.string.textLogOutNo, null)
                .show();
    }

}
