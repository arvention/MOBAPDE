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
import com.logmedown.activity.UserViewActivity;
import com.logmedown.model.User;
import com.logmedown.model.User;

import java.util.ArrayList;

public class SearchUserRecyclerAdapter extends RecyclerView.Adapter<SearchUserRecyclerAdapter.SearchUserViewHolder>{
    private ArrayList<User> users;
    private Activity main;
    private User loggedUser;

    public SearchUserRecyclerAdapter(Activity main, ArrayList<User> users, User loggedUser){
        this.main = main;
        this.users = users;
        this.loggedUser = loggedUser;
    }

    @Override
    public SearchUserRecyclerAdapter.SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent,false);
        final SearchUserViewHolder searchUserViewHolder = new SearchUserViewHolder(view);

        return searchUserViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchUserViewHolder holder, final int position) {
        User user = users.get(position);
        holder.name.setText(user.getFirstName() + " " + user.getLastName());
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                User user = users.get(position);
                Intent intent = new Intent(main, UserViewActivity.class);
                intent.putExtra("view_user", user);
                intent.putExtra("logged_user", loggedUser);
                main.startActivity(intent);
            }
        });
        holder.profileImage.setImageResource(R.drawable.profile_img);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateList(ArrayList<User> users){
        this.users = new ArrayList<>();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    public static class SearchUserViewHolder extends RecyclerView.ViewHolder{
        //components
        TextView name;
        CardView cardView;
        ImageView profileImage;

        public SearchUserViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.search_user_name);
            cardView = (CardView) itemView.findViewById(R.id.search_user_card_layout);
            profileImage = (ImageView) itemView.findViewById(R.id.search_user_user_image);
        }
    }
}
