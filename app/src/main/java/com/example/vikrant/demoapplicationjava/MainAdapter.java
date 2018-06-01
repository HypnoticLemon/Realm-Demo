package com.example.vikrant.demoapplicationjava;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RowHolder> {

    private Context context;
    private RealmResults<RealmDataModel> realmDataModels;

    public MainAdapter(Context context, RealmResults<RealmDataModel> realmDataModels) {
        this.context = context;
        this.realmDataModels = realmDataModels;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RowHolder(LayoutInflater.from(context).inflate(R.layout.single_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        holder.txtBody.setText(realmDataModels.get(position).getBody());
        holder.txtTitle.setText(realmDataModels.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (realmDataModels != null ? realmDataModels.size() : 0);
    }

    public class RowHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView txtTitle, txtBody;


        public RowHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtBody = itemView.findViewById(R.id.txtBody);

        }
    }
}
