package com.company.verbzz_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.company.verbzz_app.R;

public class ShoppingFragment extends Fragment {

    TextView noProducts;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        noProducts = view.findViewById(R.id.disableAds);

        //if you decide to add products, such as disabling adds or buying verbs, it can be done here

        return view;
    }

}