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
import com.logmedown.activity.BlocViewActivity;
import com.logmedown.database.Database;
import com.logmedown.model.Bloc;
import com.logmedown.model.User;

import java.util.ArrayList;

public class BlocRecyclerAdapter extends RecyclerView.Adapter<BlocRecyclerAdapter.BlocViewHolder>{

    private User loggedUser;
    private Database db;

    private ArrayList<Bloc> blocs;
    private Activity main;
    private RecyclerView recyclerView;

    public BlocRecyclerAdapter(ArrayList<Bloc> blocs, Activity main, User loggedUser, RecyclerView recyclerView){
        this.blocs = blocs;
        this.main = main;
        this.loggedUser = loggedUser;
        db = Database.getInstance(main);
        this.recyclerView = recyclerView;
    }

    @Override
    public BlocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloc_card_layout, parent,false);
        final BlocViewHolder noteViewHolder = new BlocViewHolder(view);

        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(BlocViewHolder holder, final int position) {
        // set image later
        holder.blocName.setText(blocs.get(position).getName());
        holder.blocType.setText(blocs.get(position).getType());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlocRecyclerAdapter.this.main, BlocViewActivity.class);

                Bloc bloc = blocs.get(position);
                db.fillBlocDetails(bloc, loggedUser);
                bloc.setMembers(db.getMembersOfBloc(bloc));
                bloc.setNotes(db.getNotes(bloc));
                blocs.set(position, bloc);

                intent.putExtra("logged_user", BlocRecyclerAdapter.this.loggedUser);
                intent.putExtra("bloc", blocs.get(position));
                intent.putExtra("position", position);
                BlocRecyclerAdapter.this.main.startActivityForResult(intent, 4);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blocs.size();
    }

    public static class BlocViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView blocImage;
        TextView blocName;
        TextView blocType;

        public BlocViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_layout);
            blocImage = (ImageView) itemView.findViewById(R.id.bloc_image);
            blocName = (TextView) itemView.findViewById(R.id.bloc_name);
            blocType = (TextView) itemView.findViewById(R.id.bloc_type);
        }
    }
}
