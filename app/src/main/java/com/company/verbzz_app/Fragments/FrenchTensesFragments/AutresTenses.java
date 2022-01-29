package com.company.verbzz_app.Fragments.FrenchTensesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.FrenchModelClasses.ModelClassFrench;
import com.company.verbzz_app.Classes.VerbEventBus;
import com.company.verbzz_app.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AutresTenses extends Fragment {

    TextView infinitifPresent, infinitifPasse, participePresent, participePasse
            , imperatifPrTu, imperatifPrNous, imperatifPrVous
            ,imperatifPaTu, imperatifPaNous, imperatifPaVous;
    String verb;
    int index;

    public AutresTenses() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_autres_tenses, container, false);

        infinitifPresent = view.findViewById(R.id.infinitifView);
        infinitifPasse = view.findViewById(R.id.infinitifView2);
        participePresent = view.findViewById(R.id.participeView);
        participePasse = view.findViewById(R.id.participeView2);
        imperatifPrTu = view.findViewById(R.id.imperatifTuView);
        imperatifPrNous = view.findViewById(R.id.imperatifNousView);
        imperatifPrVous = view.findViewById(R.id.imperatifVousView);
        imperatifPaTu = view.findViewById(R.id.imperatifTuView2);
        imperatifPaNous = view.findViewById(R.id.imperatifNousView2);
        imperatifPaVous = view.findViewById(R.id.imperatifVousView2);

        DatabaseAccess databaseAccess = new DatabaseAccess();
        databaseAccess.callRetrofitFrench(data -> {
            ModelClassFrench verbData = data.get(index);
            infinitifPresent.setText(verbData.getInfinitif().getPrSent().get(0));
            infinitifPasse.setText(verbData.getInfinitif().getPass().get(0));
            participePresent.setText(verbData.getParticipe().getPrSent().get(0));
            participePasse.setText(verbData.getParticipe().getPass().get(0));
            imperatifPrTu.setText(verbData.getImpRatif().getPrSent().get(0));
            imperatifPrNous.setText(verbData.getImpRatif().getPrSent().get(1));
            imperatifPrVous.setText(verbData.getImpRatif().getPrSent().get(2));
            imperatifPaTu.setText(verbData.getImpRatif().getPass().get(0));
            imperatifPaNous.setText(verbData.getImpRatif().getPass().get(1));
            imperatifPaVous.setText(verbData.getImpRatif().getPass().get(2));
        });

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

}