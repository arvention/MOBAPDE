package com.logmedown.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.model.User;

import java.util.ArrayList;

public class AddBlocMemberRecyclerAdapter extends RecyclerView.Adapter<AddBlocMemberRecyclerAdapter.AddBlocViewHolder>{

    private ArrayList<User> friends;
    private Activity main;
    private RecyclerView recyclerView;
    private ArrayList<User> selectedFriends;

    public AddBlocMemberRecyclerAdapter(ArrayList<User> friends, Activity main, RecyclerView recyclerView){
        this.friends = friends;
        this.main = main;
        this.recyclerView = recyclerView;

        this.selectedFriends = new ArrayList<>();
    }

    @Override
    public AddBlocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloc_card_add_member_layout, parent,false);
        final AddBlocViewHolder addBlocViewHolder = new AddBlocViewHolder(view);

        return addBlocViewHolder;
    }

    @Override
    public void onBindViewHolder(final AddBlocViewHolder holder, int position) {
        // edit here set dynamic user image
        holder.userName.setText(friends.get(position).getFirstName() + " " + friends.get(position).getLastName());
        holder.userUsername.setText(friends.get(position).getUsername());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.cardView.isSelected()){
                    holder.cardView.setSelected(true);
                    holder.userCheckBox.setChecked(true);
                    selectedFriends.add(friends.get(holder.getAdapterPosition()));
                }
                else{
                    holder.cardView.setSelected(false);
                    holder.userCheckBox.setChecked(false);
                    selectedFriends.remove(friends.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public ArrayList<User> getSelectedFriends(){ return selectedFriends; }

    public static class AddBlocViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        CheckBox userCheckBox;
        ImageView userImage;
        TextView userName;
        TextView userUsername;

        public AddBlocViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_layout);
            userCheckBox = (CheckBox) itemView.findViewById(R.id.user_check_box);
            userCheckBox.setChecked(false);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userUsername = (TextView) itemView.findViewById(R.id.user_username);
        }
    }
}
