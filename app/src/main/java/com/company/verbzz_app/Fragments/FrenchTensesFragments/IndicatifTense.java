package com.company.verbzz_app.Fragments.FrenchTensesFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Adapters.ConjugationAdapterFrench;
import com.company.verbzz_app.Classes.EventBusClasses.VerbEventBus;
import com.company.verbzz_app.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class IndicatifTense extends Fragment {

    private final ArrayList<String> conjugations = new ArrayList<>();
    String verb;
    int index;

    public IndicatifTense() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_indicatif_tense, container, false);

        inflateConjugations();

        RecyclerView recyclerView = view.findViewById(R.id.indicatifRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ConjugationAdapterFrench adapter = new ConjugationAdapterFrench(conjugations, view.getContext(), verb, index);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(VerbEventBus verbEventBus) {
        verb = verbEventBus.getVerb();
        index = verbEventBus.getIndex();
    }

    public void inflateConjugations() {
        conjugations.add("Présent");
        conjugations.add("Passé Composé");
        conjugations.add("Imparfait");
        conjugations.add("Plus-Que-Parfait");
        conjugations.add("Passé Simple");
        conjugations.add("Passé Antérieur");
        conjugations.add("Futur Simple");
        conjugations.add("Futur Antérieur");
    }
}