package com.logmedown.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.model.User;
import com.logmedown.model.User;

import java.util.ArrayList;

public class SearchUserRecyclerAdapter extends RecyclerView.Adapter<SearchUserRecyclerAdapter.SearchUserViewHolder>{
    private ArrayList<User> users;

    public SearchUserRecyclerAdapter(ArrayList<User> users){
        this.users = users;
    }

    @Override
    public SearchUserRecyclerAdapter.SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent,false);
        final SearchUserViewHolder searchUserViewHolder = new SearchUserViewHolder(view);

        return searchUserViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchUserViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getFirstName() + " " + user.getLastName());
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

        public SearchUserViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.search_user_name);
        }
    }
}
