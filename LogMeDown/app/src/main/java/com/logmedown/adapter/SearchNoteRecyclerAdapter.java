package com.logmedown.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.model.Note;

import java.util.ArrayList;

public class SearchNoteRecyclerAdapter extends RecyclerView.Adapter<SearchNoteRecyclerAdapter.SearchNoteViewHolder>{
    private ArrayList<Note> notes;

    public SearchNoteRecyclerAdapter(ArrayList<Note> notes){
        this.notes = notes;
    }

    @Override
    public SearchNoteRecyclerAdapter.SearchNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_note, parent,false);
        final SearchNoteViewHolder searchNoteViewHolder = new SearchNoteViewHolder(view);

        return searchNoteViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchNoteViewHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
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
        //components
        TextView title;

        public SearchNoteViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.search_note_title);
        }
    }
}
