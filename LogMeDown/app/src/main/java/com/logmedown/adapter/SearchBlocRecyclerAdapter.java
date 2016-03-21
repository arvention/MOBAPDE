package com.logmedown.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.model.Bloc;

import java.util.ArrayList;

public class SearchBlocRecyclerAdapter extends RecyclerView.Adapter<SearchBlocRecyclerAdapter.SearchBlocViewHolder>{
    private ArrayList<Bloc> blocs;

    public SearchBlocRecyclerAdapter(ArrayList<Bloc> blocs){
        this.blocs = blocs;
    }

    @Override
    public SearchBlocRecyclerAdapter.SearchBlocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_bloc, parent,false);
        final SearchBlocViewHolder searchBlocViewHolder = new SearchBlocViewHolder(view);

        return searchBlocViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchBlocViewHolder holder, int position) {
        holder.name.setText(blocs.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return blocs.size();
    }

    public void updateList(ArrayList<Bloc> blocs){
        this.blocs = new ArrayList<>();
        this.blocs.addAll(blocs);
        notifyDataSetChanged();
    }

    public static class SearchBlocViewHolder extends RecyclerView.ViewHolder{
        //components
        TextView name;

        public SearchBlocViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.search_bloc_name);
        }
    }
}
