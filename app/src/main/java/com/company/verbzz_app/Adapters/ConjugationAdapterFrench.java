package com.company.verbzz_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.FrenchModelClasses.ModelClassFrench;
import com.company.verbzz_app.Classes.OnListLoaded;
import com.company.verbzz_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConjugationAdapterFrench extends RecyclerView.Adapter<ConjugationAdapterFrench.ConjugationAdapterFrenchViewHolder>{
    /*This adapter is used by French Fragment to set the model of the recycler view that
    updates all conjugations*/

    ArrayList<String> conjugations;
    Context context;
    String verb;
    int index;
    private final DatabaseAccess databaseAccess = new DatabaseAccess();

    public ConjugationAdapterFrench(ArrayList<String> conjugations, Context context, String verb, int index) {
        this.conjugations = conjugations;
        this.context = context;
        this.verb = verb;
        this.index = index;
    }

    @NonNull
    @Override
    public ConjugationAdapterFrenchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.card_conjugated_verb, parent, false);
        return new ConjugationAdapterFrenchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConjugationAdapterFrenchViewHolder holder, int position) {
        returnConjugation(conjugations.get(position), list -> CompletableFuture.runAsync(() -> {
            holder.tenseView.setText(conjugations.get(position));
            holder.iView.setText(format(position, list, 0));
            holder.youView.setText(format(position, list, 1));
            holder.heSheView.setText(format(position, list, 2));
            holder.weView.setText(format(position, list, 3));
            holder.youPluralView.setText(format(position, list, 4));
            holder.theyView.setText(format(position, list, 5));
        }));
    }

    @Override
    public int getItemCount() {
        return conjugations.size();
    }

    public static class ConjugationAdapterFrenchViewHolder extends RecyclerView.ViewHolder {
        TextView tenseView, iView, youView, heSheView, weView, youPluralView, theyView;

        public ConjugationAdapterFrenchViewHolder(@NonNull View itemView) {
            super(itemView);
            tenseView = itemView.findViewById(R.id.tenseView);
            iView = itemView.findViewById(R.id.iTextView);
            youView = itemView.findViewById(R.id.youTextView);
            heSheView = itemView.findViewById(R.id.heSheItView);
            weView = itemView.findViewById(R.id.weView);
            youPluralView = itemView.findViewById(R.id.youPluralView);
            theyView = itemView.findViewById(R.id.theyView);
        }
    }

    //Accesses database to retrieve french list of conjugations of a specific verb;
    public void returnConjugation(String tense, OnListLoaded onListLoaded) {
        databaseAccess.callRetrofitFrench(data -> {
            //model class created for retrofit;
            ModelClassFrench verbData = data.get(index);
            //Interface used to allow this data to be used outside of this asynchronous function;
            onListLoaded.onListLoaded(returnVerbList(verbData, tense));
        });
    }

    //returns a list of verbs conjugated, one item for each pronoun;
    private List<String> returnVerbList(ModelClassFrench verbData, String tense) {
        return databaseAccess.returnVerbListFrench(verbData, tense);
    }

    //Returns pronouns list that applies to different french verb tenses;
    private String[] returnPronounList(String tense) {
        return databaseAccess.returnPronounListFrench(tense, verb);
    }

    //Formats the string to be displayed in each line in the card view (in recycler view);
    public String format(int position, List<String> list, int index) {
        return String.format("%s%s", returnPronounList(conjugations.get(position))[index], list.get(index));
    }

}
