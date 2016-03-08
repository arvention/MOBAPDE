package com.example.arces.logmedown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Arces on 08/03/2016.
 */
public class NoteListRecyclerAdapter extends RecyclerView.Adapter<NoteListRecyclerAdapter.NoteViewHolder> {

    private static final String NOTEPREFERENCES = "NotePreferences";
    private ArrayList<Note> noteList;
    private OnItemClickListener mOnItemClickListener;
    private LayoutInflater inflater;
    private Activity main;
    private SharedPreferences sharedPreferences;

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
    public NoteListRecyclerAdapter(Context context, final ArrayList<Note> noteList){
        this.noteList = noteList;
        this.inflater = LayoutInflater.from(context);
        this.main = (Activity) context;

        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int positionOfItemClicked) {
                Note note = noteList.get(positionOfItemClicked);

                Log.d("debug_recycler", note.getTitle());
                Log.d("debug_recycler", main.getTitle().toString());

                Intent intent = new Intent(main, NoteActivity.class);
                intent.putExtra("note_action", "edit");
                intent.putExtra("note_details", note);

                main.startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.container.setTag(holder);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(((NoteViewHolder) v.getTag()).getAdapterPosition());
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int positionOfItemClicked);
    }
}
