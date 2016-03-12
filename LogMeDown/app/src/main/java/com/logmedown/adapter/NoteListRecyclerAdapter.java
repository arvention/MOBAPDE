package com.logmedown.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.arces.logmedown.R;
import com.logmedown.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Arces on 08/03/2016.
 */
public class NoteListRecyclerAdapter extends RecyclerView.Adapter<NoteListRecyclerAdapter.NoteViewHolder> {

    private ArrayList<Note> noteList = new ArrayList<>();
    private OnItemClickListener clicker;
    private LayoutInflater inflater;
    private Activity main;

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        View container;
        TextView title, date;
        public NoteViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            title = (TextView) itemView.findViewById(R.id.note_title);
            date = (TextView) itemView.findViewById(R.id.note_date);
        }
    }

    /*-- CONSTRUCTOR --*/
    public NoteListRecyclerAdapter(Fragment fragment, Context context, final ArrayList<Note> noteList){
        if(noteList != null){
            this.noteList = noteList;
        }else{
            this.noteList = new ArrayList<>();
        }
        this.inflater = LayoutInflater.from(context);
        this.main = (Activity) context;
        this.clicker = (OnItemClickListener) fragment;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        final Note note = noteList.get(position);

        holder.container.setTag(holder);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  holder.date.setTextColor(main.getResources().getColor(R.color.colorWhite));
              //  holder.container.setBackgroundColor(main.getResources().getColor(R.color.colorGray));
                clicker.onItemClick(position, note);
            }
        });

        holder.title.setText(note.getTitle());

        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        holder.date.setText(df.format(note.getDate()));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position, Note note);
    }
}
