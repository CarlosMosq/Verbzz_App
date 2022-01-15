package com.company.verbzz_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.verbzz_app.R;

public class BlankHoneyFragment extends Fragment {

    ImageView image;
    TextView text;

    public BlankHoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank_honey, container, false);

        image = view.findViewById(R.id.honeyCombBlank);
        text = view.findViewById(R.id.app_title_blank);

        return view;
    }
}