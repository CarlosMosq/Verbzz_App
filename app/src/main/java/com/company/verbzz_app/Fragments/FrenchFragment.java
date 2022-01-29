package com.company.verbzz_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.verbzz_app.Adapters.TensesAdapter;
import com.company.verbzz_app.R;

import java.util.ArrayList;

public class FrenchFragment extends Fragment {

    private final ArrayList<String> tenses = new ArrayList<>();

    public FrenchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_french, container, false);

        fillVerbArray(tenses);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerTensesFrench);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        TensesAdapter adapter = new TensesAdapter(tenses, view.getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void fillVerbArray(ArrayList<String> tenses) {
        //these three will have their own separate fragment
        tenses.add("Indicatif");
        tenses.add("Conditionnel");
        tenses.add("Subjonctif");
        //these 3 will point to the same fragment;
        tenses.add("Impératif");
        tenses.add("Participe");
        tenses.add("Infinitif");
    }
}