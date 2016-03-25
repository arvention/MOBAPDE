package com.logmedown.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.activity.NoteViewActivity;
import com.logmedown.model.Note;

import java.util.ArrayList;

public class SearchNoteRecyclerAdapter extends RecyclerView.Adapter<SearchNoteRecyclerAdapter.SearchNoteViewHolder>{
    private ArrayList<Note> notes;
    private Activity main;

    public SearchNoteRecyclerAdapter(ArrayList<Note> notes, Activity main){
        this.notes = notes;
        this.main = main;
    }

    @Override
    public SearchNoteRecyclerAdapter.SearchNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_profile_layout, parent,false);
        final SearchNoteViewHolder searchNoteViewHolder = new SearchNoteViewHolder(view);

        return searchNoteViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchNoteViewHolder holder, final int position) {
        if(notes.get(position).getBloc() == null)
            holder.userName.setText(notes.get(position).getCreator().getFirstName() + " " + notes.get(position).getCreator().getLastName());
        else // fix this
            holder.userName.setText(notes.get(position).getCreator().getFirstName() + " " + notes.get(position).getCreator().getLastName() + " > " + notes.get(position).getBloc().getName());

        holder.userPostTitle.setText(notes.get(position).getTitle());
        holder.userImage.setImageResource(R.drawable.profile_img);

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //view note
                Note note = notes.get(position);
                Intent intent = new Intent(main, NoteViewActivity.class);
                intent.putExtra("note", note);
                main.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void updateList(ArrayList<Note> notes){
        this.notes = new ArrayList<>();
        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    public static class SearchNoteViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView userName;
        TextView userPostTitle;
        ImageView userImage;

        public SearchNoteViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_layout);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userPostTitle = (TextView) itemView.findViewById(R.id.user_post_title);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);

            itemView.setClickable(true);
        }
    }
}
