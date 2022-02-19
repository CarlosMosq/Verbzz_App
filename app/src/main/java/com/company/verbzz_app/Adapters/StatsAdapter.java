package com.company.verbzz_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Classes.Stats;
import com.company.verbzz_app.R;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.statsViewHolder> {
    Context context;
    List<Stats> list;

    public StatsAdapter(Context context, List<Stats> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public statsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_stats_table, parent, false);
        return new statsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull statsViewHolder holder, int position) {
        Stats stats = list.get(position);
        String initializer;
        if(stats.getLanguage().equals("English")) {
            initializer = "En";
        }
        else {
            initializer = "Fr";
        }
        holder.languageTense.setText(String.format("(%s) %s", initializer, stats.getTense()));
        holder.date.setText(stats.getDate());
        holder.score.setText(stats.getScore());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class statsViewHolder extends RecyclerView.ViewHolder {
        TextView languageTense, score, date;

        public statsViewHolder(@NonNull View itemView) {
            super(itemView);
            languageTense = itemView.findViewById(R.id.activityValue);
            score = itemView.findViewById(R.id.scoreValue);
            date = itemView.findViewById(R.id.dateValue);
        }
    }


}
