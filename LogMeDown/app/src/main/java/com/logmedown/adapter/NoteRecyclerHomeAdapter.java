package com.logmedown.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.activity.NoteActivity;
import com.logmedown.activity.NoteViewActivity;
import com.logmedown.database.Database;
import com.logmedown.model.Note;
import com.logmedown.model.User;

import java.util.ArrayList;

public class NoteRecyclerHomeAdapter extends RecyclerView.Adapter<NoteRecyclerHomeAdapter.NoteViewHolder> {

    private Database db;

    private ArrayList<Note> notes;
    private Activity main;
    private RecyclerView recyclerView;
    private User user;

    public NoteRecyclerHomeAdapter(ArrayList<Note> notes, Activity main, RecyclerView recyclerView, User user){

        this.db = Database.getInstance(main);

        this.notes = notes;
        this.main = main;
        this.recyclerView = recyclerView;
        this.user = user;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_home_layout, parent,false);
        final NoteViewHolder noteViewHolder = new NoteViewHolder(view);

        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        if(notes.get(position).getBloc() == null)
            holder.userName.setText(notes.get(position).getCreator().getFirstName() + " " + notes.get(position).getCreator().getLastName() + " > Public");
        else // fix this
            holder.userName.setText(notes.get(position).getCreator().getFirstName() + " " + notes.get(position).getCreator().getLastName() + " > ");

        holder.userPostTitle.setText(notes.get(position).getTitle());
        holder.userImage.setImageResource(R.drawable.profile_img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteRecyclerHomeAdapter.this.main, NoteViewActivity.class);
                intent.putExtra("note", notes.get(position));
                NoteRecyclerHomeAdapter.this.main.startActivity(intent);
            }
        });

        if(user.getUserID() == notes.get(position).getCreator().getUserID()) {
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NoteRecyclerHomeAdapter.this.main, NoteActivity.class);
                    intent.putExtra("note_action", "edit");
                    intent.putExtra("note_details", notes.get(position));
                    intent.putExtra("position", position);

                    NoteRecyclerHomeAdapter.this.main.startActivityForResult(intent, 1);
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.deleteNote(NoteRecyclerHomeAdapter.this.notes.get(position));
                    NoteRecyclerHomeAdapter.this.notes.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }else{
            holder.deleteButton.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView userName;
        TextView userPostTitle;
        ImageView userImage;
        ImageButton editButton;
        ImageButton deleteButton;

        public NoteViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.home_card_layout);
            userName = (TextView) itemView.findViewById(R.id.home_user_name);
            userPostTitle = (TextView) itemView.findViewById(R.id.home_user_post_title);
            userImage = (ImageView) itemView.findViewById(R.id.home_user_image);

            editButton = (ImageButton) itemView.findViewById(R.id.home_noteEditButton);
            deleteButton = (ImageButton) itemView.findViewById(R.id.home_noteDeleteButton);

            itemView.setClickable(true);
        }
    }
}
