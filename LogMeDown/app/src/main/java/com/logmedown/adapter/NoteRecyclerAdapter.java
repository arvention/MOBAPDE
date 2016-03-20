package com.logmedown.adapter;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.database.Database;
import com.logmedown.model.Note;

import java.util.ArrayList;
import java.util.Collections;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {

    private Database db;

    private ArrayList<Note> notes;
    private Activity main;

    private ArrayList<Integer> selectedItems;
    private Animation zoomIn;
    private Animation zoomOut;
    private Animation slideDown;
    private Animation slideUp;

    private LinearLayout noteActionMenu;
    private ImageButton editNoteButton;
    private ImageButton viewNoteButton;
    private ImageButton deleteNoteButton;

    private RecyclerView recyclerView;

    public NoteRecyclerAdapter(ArrayList<Note> notes, Activity main, LinearLayout noteActionMenu, ImageButton editNoteButton,
                               ImageButton viewNoteButton, ImageButton deleteNoteButton, RecyclerView recyclerView){

        this.notes = notes;

        this.main = main;
        slideDown = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.slide_up);
        zoomIn = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.zoom_out);

        db = Database.getInstance(main.getApplicationContext());

        zoomOut.setStartOffset(100);
        zoomOut.setDuration(100);
        zoomIn.setDuration(100);
        slideDown.setStartOffset(100);

        this.noteActionMenu = noteActionMenu;
        this.editNoteButton = editNoteButton;
        this.viewNoteButton = viewNoteButton;
        this.deleteNoteButton = deleteNoteButton;

        selectedItems = new ArrayList<>();

        this.recyclerView = recyclerView;

        this.deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.sort(selectedItems, Collections.reverseOrder());
                for(int i = 0; i < selectedItems.size(); i++){
                    Log.d("DELETE", NoteRecyclerAdapter.this.notes.get((int) selectedItems.get(i)).getTitle());
                    db.deleteNote(NoteRecyclerAdapter.this.notes.get((int) selectedItems.get(i)));
                    NoteRecyclerAdapter.this.notes.remove((int) selectedItems.get(i));
                    notifyItemRemoved(selectedItems.get(i));
                }

                selectedItems = new ArrayList<>();
                closeNoteActionMenu();
            }
        });
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout, parent,false);
        final NoteViewHolder noteViewHolder = new NoteViewHolder(view);

        noteViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noteViewHolder.cardView.isSelected()) {
                    selectedItems.add(noteViewHolder.getAdapterPosition());
                    noteViewHolder.cardView.setSelected(true);
                    noteViewHolder.cardView.setBackgroundColor(ContextCompat.getColor(main.getApplicationContext(), R.color.colorAccent));
                } else if (noteViewHolder.cardView.isSelected()) {
                    selectedItems.remove(selectedItems.indexOf(noteViewHolder.getAdapterPosition()));
                    noteViewHolder.cardView.setSelected(false);
                    noteViewHolder.cardView.setBackgroundColor(ContextCompat.getColor(main.getApplicationContext(), R.color.colorCardView));
                }

                if (selectedItems.size() == 1 && noteActionMenu.getVisibility() != View.VISIBLE) {
                    noteActionMenu.setVisibility(View.VISIBLE);
                    noteActionMenu.startAnimation(slideUp);

                    editNoteButton.setVisibility(View.VISIBLE);
                    editNoteButton.startAnimation(zoomOut);
                    viewNoteButton.setVisibility(View.VISIBLE);
                    viewNoteButton.startAnimation(zoomOut);
                    deleteNoteButton.setVisibility(View.VISIBLE);
                    deleteNoteButton.startAnimation(zoomOut);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)recyclerView.getLayoutParams();
                    lp.height = recyclerView.getMeasuredHeight() - noteActionMenu.getHeight();
                    recyclerView.setLayoutParams(lp);

                } else if (selectedItems.size() == 1 && noteActionMenu.getVisibility() == View.VISIBLE) {
                    editNoteButton.setVisibility(View.VISIBLE);
                    editNoteButton.startAnimation(zoomOut);
                    viewNoteButton.setVisibility(View.VISIBLE);
                    viewNoteButton.startAnimation(zoomOut);
                } else if (selectedItems.size() > 1 && noteActionMenu.getVisibility() == View.VISIBLE) {
                    if (editNoteButton.getVisibility() == View.VISIBLE && viewNoteButton.getVisibility() == View.VISIBLE) {
                        editNoteButton.setVisibility(View.GONE);
                        editNoteButton.startAnimation(zoomIn);
                        viewNoteButton.setVisibility(View.GONE);
                        viewNoteButton.startAnimation(zoomIn);
                        deleteNoteButton.setVisibility(View.GONE);
                        deleteNoteButton.startAnimation(zoomIn);
                    }
                    if (deleteNoteButton.getVisibility() == View.GONE) {
                        deleteNoteButton.setVisibility(View.VISIBLE);
                        deleteNoteButton.startAnimation(zoomOut);
                    }
                } else if (selectedItems.size() == 0 && noteActionMenu.getVisibility() == View.VISIBLE) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)recyclerView.getLayoutParams();
                    lp.height = recyclerView.getMeasuredHeight() + noteActionMenu.getHeight();
                    recyclerView.setLayoutParams(lp);
                    closeNoteActionMenu();
                }
            }
        });
        return  noteViewHolder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Log.d("BIND", String.valueOf(position));

        if(notes.get(position).getBloc() == null)
            holder.userName.setText(notes.get(position).getCreator().getFirstName() + " " + notes.get(position).getCreator().getLastName() + " > Public");
        else // fix this
            holder.userName.setText(notes.get(position).getCreator().getFirstName() + " " + notes.get(position).getCreator().getLastName() + " > ");

        holder.userPostTitle.setText(notes.get(position).getTitle());
        holder.userImage.setImageResource(R.drawable.profile_img);

        holder.cardView.setSelected(selectedItems.contains(position));

        if(holder.cardView.isSelected())
            holder.cardView.setBackgroundColor(ContextCompat.getColor(main.getApplicationContext(), R.color.colorAccent));
        else
            holder.cardView.setBackgroundColor(ContextCompat.getColor(main.getApplicationContext(), R.color.colorCardView));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private void closeNoteActionMenu(){

        if(editNoteButton.getVisibility() == View.VISIBLE && viewNoteButton.getVisibility() == View.VISIBLE){
            deleteNoteButton.setVisibility(View.GONE);
            deleteNoteButton.startAnimation(zoomIn);
            editNoteButton.setVisibility(View.GONE);
            editNoteButton.startAnimation(zoomIn);
            viewNoteButton.setVisibility(View.GONE);
            viewNoteButton.startAnimation(zoomIn);
        }

        noteActionMenu.setVisibility(View.GONE);
        noteActionMenu.startAnimation(slideDown);
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

