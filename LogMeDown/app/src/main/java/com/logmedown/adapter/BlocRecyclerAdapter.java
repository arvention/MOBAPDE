package com.logmedown.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arces.logmedown.R;
import com.logmedown.model.Bloc;

import java.util.ArrayList;

public class BlocRecyclerAdapter extends RecyclerView.Adapter<BlocRecyclerAdapter.BlocViewHolder>{

    private ArrayList<Bloc> blocs;
    private Activity main;
    private RecyclerView recyclerView;

    public BlocRecyclerAdapter(ArrayList<Bloc> blocs, Activity main, RecyclerView recyclerView){
        this.blocs = blocs;
        this.main = main;
        this.recyclerView = recyclerView;
    }

    @Override
    public BlocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloc_card_layout, parent,false);
        final BlocViewHolder noteViewHolder = new BlocViewHolder(view);

        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(BlocViewHolder holder, int position) {
        // set image later
        holder.blocName.setText(blocs.get(position).getName());
        holder.blocType.setText(blocs.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return blocs.size();
    }

    public static class BlocViewHolder extends RecyclerView.ViewHolder{

        ImageView blocImage;
        TextView blocName;
        TextView blocType;

        public BlocViewHolder(View itemView) {
            super(itemView);

            blocImage = (ImageView) itemView.findViewById(R.id.bloc_image);
            blocName = (TextView) itemView.findViewById(R.id.bloc_name);
            blocType = (TextView) itemView.findViewById(R.id.bloc_type);
        }
    }
}
