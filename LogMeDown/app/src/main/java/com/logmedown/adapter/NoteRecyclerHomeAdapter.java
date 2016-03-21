package com.logmedown.adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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

    private Animation zoomIn;
    private Animation zoomOut;
    private Animation slideDown;
    private Animation slideUp;

    private LinearLayout noteActionMenu;
    private ImageButton editNoteButton;
    private ImageButton viewNoteButton;
    private ImageButton deleteNoteButton;

    private int selectedItem;

    public NoteRecyclerHomeAdapter(ArrayList<Note> notes, Activity main, LinearLayout noteActionMenu, ImageButton editNoteButton,
                                   ImageButton viewNoteButton, ImageButton deleteNoteButton, RecyclerView recyclerView, User user){

        this.db = Database.getInstance(main);

        this.notes = notes;
        this.main = main;

        slideDown = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.slide_up);
        zoomIn = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(main.getApplicationContext(), R.anim.zoom_out);

        zoomOut.setStartOffset(100);
        zoomOut.setDuration(100);
        zoomIn.setDuration(100);
        slideDown.setStartOffset(100);

        this.noteActionMenu = noteActionMenu;
        this.editNoteButton = editNoteButton;
        this.viewNoteButton = viewNoteButton;
        this.deleteNoteButton = deleteNoteButton;

        this.recyclerView = recyclerView;
        this.user = user;

        this.deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("POS DEL", String.valueOf(selectedItem));

                db.deleteNote(NoteRecyclerHomeAdapter.this.notes.get(selectedItem));
                NoteRecyclerHomeAdapter.this.notes.remove(selectedItem);
                notifyItemRemoved(selectedItem);

                selectedItem = -1;
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)NoteRecyclerHomeAdapter.this.recyclerView.getLayoutParams();
                lp.height = NoteRecyclerHomeAdapter.this.recyclerView.getMeasuredHeight() + NoteRecyclerHomeAdapter.this.noteActionMenu.getHeight();
                NoteRecyclerHomeAdapter.this.recyclerView.setLayoutParams(lp);
                closeNoteActionMenu();
            }
        });

        this.viewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteRecyclerHomeAdapter.this.main, NoteViewActivity.class);
                intent.putExtra("note", NoteRecyclerHomeAdapter.this.notes.get(selectedItem));
                NoteRecyclerHomeAdapter.this.main.startActivity(intent);
            }
        });

        this.editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteRecyclerHomeAdapter.this.main, NoteActivity.class);
                intent.putExtra("note_action", "edit");
                intent.putExtra("note_details", NoteRecyclerHomeAdapter.this.notes.get(selectedItem));
                intent.putExtra("position", selectedItem);

                NoteRecyclerHomeAdapter.this.main.startActivityForResult(intent, 1);
            }
        });

        selectedItem = -1;
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

        holder.postContents.setText(notes.get(position).getContent());

        if(notes.get(holder.getAdapterPosition()).getCreator().getUserID() == user.getUserID()) {
            holder.openMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedItem != holder.getAdapterPosition()) {
                        selectedItem = holder.getAdapterPosition();

                        if (noteActionMenu.getVisibility() != View.VISIBLE) {
                            noteActionMenu.setVisibility(View.VISIBLE);
                            noteActionMenu.startAnimation(slideUp);

                            editNoteButton.setVisibility(View.VISIBLE);
                            editNoteButton.startAnimation(zoomOut);
                            viewNoteButton.setVisibility(View.VISIBLE);
                            viewNoteButton.startAnimation(zoomOut);
                            deleteNoteButton.setVisibility(View.VISIBLE);
                            deleteNoteButton.startAnimation(zoomOut);

                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
                            lp.height = recyclerView.getMeasuredHeight() - noteActionMenu.getHeight();
                            recyclerView.setLayoutParams(lp);
                        }
                    } else {
                        selectedItem = -1;
                        closeNoteActionMenu();

                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
                        lp.height = recyclerView.getMeasuredHeight() + noteActionMenu.getHeight();
                        recyclerView.setLayoutParams(lp);
                    }
                }
            });
        } else{
            holder.openMenuButton.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteRecyclerHomeAdapter.this.main, NoteViewActivity.class);
                intent.putExtra("note", notes.get(holder.getAdapterPosition()));
                NoteRecyclerHomeAdapter.this.main.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private void closeNoteActionMenu(){
        deleteNoteButton.setVisibility(View.GONE);
        deleteNoteButton.startAnimation(zoomIn);
        editNoteButton.setVisibility(View.GONE);
        editNoteButton.startAnimation(zoomIn);
        viewNoteButton.setVisibility(View.GONE);
        viewNoteButton.startAnimation(zoomIn);

        noteActionMenu.setVisibility(View.GONE);
        noteActionMenu.startAnimation(slideDown);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView userName;
        TextView userPostTitle;
        ImageView userImage;
        TextView postContents;
        ImageButton openMenuButton;

        public NoteViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.home_card_layout);
            userName = (TextView) itemView.findViewById(R.id.home_user_name);
            userPostTitle = (TextView) itemView.findViewById(R.id.home_user_post_title);
            userImage = (ImageView) itemView.findViewById(R.id.home_user_image);
            postContents = (TextView) itemView.findViewById(R.id.home_user_post_content);

            openMenuButton = (ImageButton) itemView.findViewById(R.id.home_noteOpenMenu);

            itemView.setClickable(true);
        }
    }
}
