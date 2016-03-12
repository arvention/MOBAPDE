package com.logmedown.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.model.Note;

import java.util.ArrayList;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {

    private ArrayList<Note> notes;

    public NoteRecyclerAdapter(ArrayList<Note> notes){
        this.notes = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout, parent,false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return  noteViewHolder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.userName.setText(notes.get(position).getCreator().getFirstName() + " " + notes.get(position).getCreator().getLastName());
        holder.userPostTitle.setText(notes.get(position).getTitle());
        holder.userImage.setImageResource(R.drawable.profile_img);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView userName;
        TextView userPostTitle;
        ImageView userImage;

        public NoteViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_layout);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userPostTitle = (TextView) itemView.findViewById(R.id.user_post_title);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);
        }
    }
}

