package com.company.verbzz_app.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Activities.Conjugate_Activity;
import com.company.verbzz_app.Fragments.Conjugated_Verb;
import com.company.verbzz_app.R;

import java.util.ArrayList;

public class TensesAdapter extends RecyclerView.Adapter<TensesAdapter.TensesViewHolder> {

    ArrayList<String> tenses;
    Context context;

    public TensesAdapter(ArrayList<String> tenses, Context context) {
        this.tenses = tenses;
        this.context = context;
    }

    @NonNull
    @Override
    public TensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verb_tenses_card, parent, false);

        return new TensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TensesViewHolder holder, int position) {
        holder.tense.setText(tenses.get(position));
        holder.cardView.setOnClickListener(view -> {

            Conjugated_Verb conjugated_verb = new Conjugated_Verb();

            Bundle bundle = new Bundle();
            bundle.putString("tense", tenses.get(position));

            replaceFragment(conjugated_verb, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return tenses.size();
    }


    public static class TensesViewHolder extends RecyclerView.ViewHolder {
        private final TextView tense;
        private final CardView cardView;

        public TensesViewHolder(@NonNull View itemView) {
            super(itemView);
            tense = itemView.findViewById(R.id.tenseName);
            cardView = itemView.findViewById(R.id.verbTense);
        }
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = ((Conjugate_Activity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conjugatedView, fragment);
        fragmentTransaction.commit();
    }
}
