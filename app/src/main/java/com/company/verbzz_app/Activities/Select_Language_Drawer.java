package com.company.verbzz_app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.company.verbzz_app.Fragments.Flags_Choose_Fragment;
import com.company.verbzz_app.R;

public class Select_Language_Drawer extends AppCompatActivity {

    private final Flags_Choose_Fragment flags = new Flags_Choose_Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language_drawer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLanguages, flags);
        fragmentTransaction.commit();

    }
}