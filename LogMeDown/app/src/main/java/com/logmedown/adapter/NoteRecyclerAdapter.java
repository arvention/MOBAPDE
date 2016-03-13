package com.logmedown.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arces.logmedown.R;
import com.logmedown.model.Note;

import java.util.ArrayList;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {

    private ArrayList<Note> notes;
    private Activity main;
    private int selectedCount;

    public NoteRecyclerAdapter(ArrayList<Note> notes, Activity main){
        this.notes = notes;
        this.main = main;
        selectedCount = 0;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout, parent,false);
        final NoteViewHolder noteViewHolder = new NoteViewHolder(view);

        return  noteViewHolder;
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
                if(!holder.isSelected) {
                    selectedCount += 1;
                    holder.isSelected = true;
                    holder.cardView.setBackgroundColor(ContextCompat.getColor(main.getApplicationContext(), R.color.colorAccent));
                }else{
                    selectedCount -= 1;
                    holder.isSelected = false;
                    holder.cardView.setBackgroundColor(ContextCompat.getColor(main.getApplicationContext(), R.color.colorCardView));
                }
                Toast.makeText(holder.cardView.getContext(), "Clicked in " + selectedCount, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void deleteNoteAt(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notes.size());
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        boolean isSelected;
        CardView cardView;
        TextView userName;
        TextView userPostTitle;
        ImageView userImage;

        public NoteViewHolder(View itemView) {
            super(itemView);
            isSelected = false;
            cardView = (CardView) itemView.findViewById(R.id.card_layout);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userPostTitle = (TextView) itemView.findViewById(R.id.user_post_title);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);

            itemView.setClickable(true);
        }
    }
}

