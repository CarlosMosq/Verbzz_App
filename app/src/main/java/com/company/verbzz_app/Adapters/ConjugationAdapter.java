package com.company.verbzz_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.EnglishModelClasses.ModelClassEnglish;
import com.company.verbzz_app.Classes.OnListLoaded;
import com.company.verbzz_app.R;

import java.util.ArrayList;
import java.util.List;

public class ConjugationAdapter extends RecyclerView.Adapter<ConjugationAdapter.ConjugationViewHolder> {

    ArrayList<String> conjugations;
    Context context;
    String verb;
    int index;


    public ConjugationAdapter(ArrayList<String> conjugations, Context context, String verb, int index) {
        this.conjugations = conjugations;
        this.context = context;
        this.verb = verb;
        this.index = index;
    }

    @NonNull
    @Override
    public ConjugationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_conjugated_verb, parent, false);

        return new ConjugationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConjugationViewHolder holder, int position) {
        holder.tenseView.setText(conjugations.get(position));
        returnConjugation(conjugations.get(position), list -> holder.iView.setText(format(position, list, 0)));
        returnConjugation(conjugations.get(position), list -> holder.youView.setText(format(position, list, 1)));
        returnConjugation(conjugations.get(position), list -> holder.heSheView.setText(format(position, list, 2)));
        returnConjugation(conjugations.get(position), list -> holder.weView.setText(format(position, list, 3)));
        returnConjugation(conjugations.get(position), list -> holder.youPluralView.setText(format(position, list, 4)));
        returnConjugation(conjugations.get(position), list -> holder.theyView.setText(format(position, list, 5)));
    }

    @Override
    public int getItemCount() {
        return conjugations.size();
    }


    public static class ConjugationViewHolder extends RecyclerView.ViewHolder {
        TextView tenseView, iView, youView, heSheView, weView, youPluralView, theyView;

        public ConjugationViewHolder(@NonNull View itemView) {
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

    public void returnConjugation(String tense, OnListLoaded onListLoaded) {
        DatabaseAccess databaseAccess = new DatabaseAccess();
        databaseAccess.callRetrofitEnglish(data -> {
            ModelClassEnglish verbData = data.get(index);
            onListLoaded.onListLoaded(returnVerbList(verbData, tense));
        });

    }



    private List<String> returnVerbList(ModelClassEnglish verbData, String tense) {
        switch (tense) {
            case "Present":
                //I work
                return verbData.getIndicative().getPresent();
            case "Past":
                //I worked
                return verbData.getIndicative().getImperfect();
            case "Future":
                //I will work
                return verbData.getIndicative().getFuture();
            case "Present Perfect":
                //I have worked
                return verbData.getIndicative().getPerfect();
            case "Past Perfect":
                //I had worked
                return verbData.getIndicative().getPlusperfect();
            case "Future Perfect":
                //I had worked
                return verbData.getIndicative().getPreviousFuture();
            case "Present Conditional":
                //I would work
                return verbData.getConditional().getConditional();
            case "Perfect Conditional":
                //I would have worked
                return verbData.getConditional().getConditionalPerfect();
            case "Present Subjunctive":
                //that I work
                return verbData.getSubjuntive().getPresent();
            case "Perfect Subjunctive":
                //I may have worked
                return verbData.getSubjuntive().getPerfect();
        }
        return null;
    }

    private String[] returnPronounList(String tense) {
        switch (tense) {
            case "Present":
            case "Past":
                //I worked
                //I work
                return new String[]{"I ", "You ", "He/she/it ", "We ", "You ", "They "};
            case "Future":
                //I will work
                return new String[]{"I will ", "You will ", "He/she/it will ", "We will ", "You will ", "They will "};
            case "Present Perfect":
                //I have worked
                return new String[]{"I have ", "You have ", "He/she/it have ", "We have ", "You have ", "They have "};
            case "Past Perfect":
                //I had worked
                return new String[]{"I had ", "You had ", "He/she/it had ", "We had ", "You had ", "They had "};
            case "Future Perfect":
                //I will have worked
                return new String[]{"I will have ", "You will have ", "He/she/it will have ", "We will have ", "You will have ", "They will have "};
            case "Present Conditional":
                //I will have worked
                return new String[]{"I would ", "You would ", "He/she/it would ", "We would ", "You would ", "They would "};
            case "Perfect Conditional":
                //I will have worked
                return new String[]{"I would have ", "You would have ", "He/she/it would have ", "We would have ", "You would have ", "They would have "};
            case "Present Subjunctive":
                //that I work
                return new String[]{"that I ", "that You ", "that He/she/it ", "that We ", "that You ", "that They "};
            case "Perfect Subjunctive":
                //that I work
                return new String[]{"I may have ", "You may have ", "He/she/it may have ", "We may have ", "You may have ", "They may have "};
        }
        return null;
    }

    public String format(int position, List<String> list, int index) {
        return String.format("%s%s", returnPronounList(conjugations.get(position))[index], list.get(index));
    }

}
