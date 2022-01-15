package com.company.verbzz_app.Fragments.EnglishTensesFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.company.verbzz_app.Classes.VerbEventBus;
import com.company.verbzz_app.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class OtherTenses extends Fragment {

    private final ArrayList<String> conjugations = new ArrayList<>();
    String verb;
    int index;

    public OtherTenses() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_other_tenses, container, false);

        inflateConjugations();


        //can't use the same adapter, create a new one
//        RecyclerView recyclerView = view.findViewById(R.id.otherRecycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        ConjugationAdapter adapter = new ConjugationAdapter(conjugations, view.getContext(), verb, index);
//        recyclerView.setAdapter(adapter);

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
        conjugations.add("Present Subjunctive");
        conjugations.add("Perfect Subjunctive");
    }
}