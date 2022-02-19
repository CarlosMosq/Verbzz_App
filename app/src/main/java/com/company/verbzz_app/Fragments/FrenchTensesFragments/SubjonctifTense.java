package com.company.verbzz_app.Fragments.FrenchTensesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.verbzz_app.Adapters.ConjugationAdapterFrench;
import com.company.verbzz_app.Classes.EventBusClasses.VerbEventBus;
import com.company.verbzz_app.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class SubjonctifTense extends Fragment {

    private final ArrayList<String> conjugations = new ArrayList<>();
    String verb;
    int index;

    public SubjonctifTense() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_subjonctif_tense, container, false);

        inflateConjugations();

        RecyclerView recyclerView = view.findViewById(R.id.subjonctifRecycler);
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
        conjugations.add("Présent Subjonctif");
        conjugations.add("Passé Subjonctif");
        conjugations.add("Imparfait Subjonctif");
        conjugations.add("Plus-que-parfait Subjonctif");
    }
}