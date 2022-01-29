package com.company.verbzz_app.Fragments.EnglishTensesFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.company.verbzz_app.Classes.DatabaseAccess;
import com.company.verbzz_app.Classes.EnglishModelClasses.ModelClassEnglish;
import com.company.verbzz_app.Classes.VerbEventBus;
import com.company.verbzz_app.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OtherTenses extends Fragment {

    TextView infinitiveText, gerundText, participleText, imperativeTextYou, imperativeTextWe, imperativeTextYouPlural;
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

        infinitiveText = view.findViewById(R.id.infinitiveView);
        gerundText = view.findViewById(R.id.gerundView);
        participleText = view.findViewById(R.id.participleView);
        imperativeTextYou = view.findViewById(R.id.imperativeViewYou);
        imperativeTextWe = view.findViewById(R.id.imperativeViewWe);
        imperativeTextYouPlural = view.findViewById(R.id.imperativeViewYouPlural);

        DatabaseAccess databaseAccess = new DatabaseAccess();
        databaseAccess.callRetrofitEnglish(data -> {
            ModelClassEnglish verbData = data.get(index);
            infinitiveText.setText(verbData.getInfinitive().get(0));
            gerundText.setText(verbData.getGerund().get(0));
            participleText.setText(verbData.getParticiple().get(0));
            imperativeTextYou.setText(verbData.getImperative().get(0));
            imperativeTextWe.setText(String.format("%s%s", "Let's", verbData.getImperative().get(1)));
            imperativeTextYouPlural.setText(verbData.getImperative().get(2));
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