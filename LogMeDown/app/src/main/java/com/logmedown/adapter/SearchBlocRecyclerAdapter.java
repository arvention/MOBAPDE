package com.logmedown.adapter;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
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

public class SearchBlocRecyclerAdapter extends RecyclerView.Adapter<SearchBlocRecyclerAdapter.SearchBlocViewHolder> {
    private ArrayList<Bloc> blocs;
    private Activity main;
    private Database db;
    private User user;

    public SearchBlocRecyclerAdapter(ArrayList<Bloc> blocs, Activity main, User user) {
        this.blocs = blocs;
        this.main = main;
        this.db = Database.getInstance(main);
        this.user = user;
    }

    @Override
    public SearchBlocRecyclerAdapter.SearchBlocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloc_card_layout, parent, false);
        final SearchBlocViewHolder searchBlocViewHolder = new SearchBlocViewHolder(view);

        return searchBlocViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchBlocViewHolder holder, final int position) {
        holder.blocName.setText(blocs.get(position).getName());
        holder.blocType.setText(blocs.get(position).getType());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, BlocViewActivity.class);

                Bloc bloc = blocs.get(position);
                db.fillBlocDetails(bloc, user);
                bloc.setMembers(db.getMembersOfBloc(bloc));
                bloc.setNotes(db.getNotes(bloc));
                blocs.set(position, bloc);

                intent.putExtra("logged_user", user);
                intent.putExtra("bloc", blocs.get(position));
                intent.putExtra("position", position);
                main.startActivityForResult(intent, 4);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blocs.size();
    }

    public void updateList(ArrayList<Bloc> blocs) {
        this.blocs = new ArrayList<>();
        this.blocs.addAll(blocs);
        notifyDataSetChanged();
    }

    public static class SearchBlocViewHolder extends RecyclerView.ViewHolder {
        //components
        CardView cardView;
        ImageView blocImage;
        TextView blocName;
        TextView blocType;

        public SearchBlocViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_layout);
            blocImage = (ImageView) itemView.findViewById(R.id.bloc_image);
            blocName = (TextView) itemView.findViewById(R.id.bloc_name);
            blocType = (TextView) itemView.findViewById(R.id.bloc_type);
        }

    }
}
