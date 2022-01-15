package com.company.verbzz_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.verbzz_app.Adapters.Choose_New_Language_Adapter;
import com.company.verbzz_app.R;

import java.util.ArrayList;

public class Flags_Choose_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String> languages;
    private Choose_New_Language_Adapter adapter;

    public Flags_Choose_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flags_choose, container, false);

        currentLanguages(languages);
        recyclerView = view.findViewById(R.id.languagesFlags);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new Choose_New_Language_Adapter(languages, view.getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void currentLanguages(ArrayList<String> languages) {
        languages.add("English");
        languages.add("French");
    }
}