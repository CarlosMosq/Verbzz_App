package com.company.verbzz_app.Adapters;

import android.content.Context;
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
import com.company.verbzz_app.Fragments.EnglishTensesFragments.ConditionalTense;
import com.company.verbzz_app.Fragments.EnglishTensesFragments.IndicativeTense;
import com.company.verbzz_app.Fragments.EnglishTensesFragments.OtherTenses;
import com.company.verbzz_app.Fragments.EnglishTensesFragments.SubjunctiveTense;
import com.company.verbzz_app.Fragments.FrenchTensesFragments.AutresTenses;
import com.company.verbzz_app.Fragments.FrenchTensesFragments.ConditionnelTense;
import com.company.verbzz_app.Fragments.FrenchTensesFragments.IndicatifTense;
import com.company.verbzz_app.Fragments.FrenchTensesFragments.SubjonctifTense;
import com.company.verbzz_app.R;

import java.util.ArrayList;

public class TensesAdapter extends RecyclerView.Adapter<TensesAdapter.TensesViewHolder> {

    ArrayList<String> tenses;
    Context context;
    private final IndicativeTense indicativeTense = new IndicativeTense();
    private final ConditionalTense conditionalTense = new ConditionalTense();
    private final SubjunctiveTense subjunctiveTense = new SubjunctiveTense();
    private final OtherTenses otherTenses = new OtherTenses();
    private final IndicatifTense indicatifTense = new IndicatifTense();
    private final ConditionnelTense conditionnelTense = new ConditionnelTense();
    private final SubjonctifTense subjonctifTense = new SubjonctifTense();
    private final AutresTenses autresTenses = new AutresTenses();

    public TensesAdapter(ArrayList<String> tenses, Context context) {
        this.tenses = tenses;
        this.context = context;
    }

    @NonNull
    @Override
    public TensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_verb_tenses, parent, false);
        return new TensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TensesViewHolder holder, int position) {
        holder.tense.setText(tenses.get(position));
        holder.cardView.setOnClickListener(view -> replaceFragment(returnFragment(tenses.get(position))));
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

    //used by English fragment and French fragment to set correct fragments to display conjugated data
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((Conjugate_Activity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conjugatedView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //returns specific fragment to be replaced in the conjugated activity
    public Fragment returnFragment(String tense) {
        switch (tense) {
            case "Indicative":
                return indicativeTense;
            case "Conditional":
                return conditionalTense;
            case "Subjunctive":
                return subjunctiveTense;
            case "Imperative":
            case "Participle":
            case "Gerund":
            case "Infinitive":
                return otherTenses;
            case "Indicatif":
                return indicatifTense;
            case "Conditionnel":
                return conditionnelTense;
            case "Subjonctif":
                return subjonctifTense;
            default:
                return autresTenses;
        }
    }

}
