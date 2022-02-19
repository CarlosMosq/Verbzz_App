package com.company.verbzz_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.company.verbzz_app.Fragments.LessonsFragment;
import com.company.verbzz_app.Fragments.ShoppingFragment;
import com.company.verbzz_app.Fragments.StatisticsFragment;
import com.company.verbzz_app.R;

public class MainActivity extends AppCompatActivity {

    ImageButton homeButton, statistics, shoppingCart;
    private final LessonsFragment lessonsFragment = new LessonsFragment();
    private final StatisticsFragment statisticsFragment = new StatisticsFragment();
    private final ShoppingFragment shoppingFragment = new ShoppingFragment();
    boolean falseForLessons = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeButton = findViewById(R.id.homeButton);
        statistics = findViewById(R.id.statisticsButton);
        shoppingCart = findViewById(R.id.shoppingCart);
        Intent i = getIntent();
        if (i != null) {
            falseForLessons = i.getBooleanExtra("setStatistics", false);
        }

        setInitialFragment(falseForLessons);

        homeButton.setOnClickListener(view -> replaceFragment(lessonsFragment));
        statistics.setOnClickListener(view -> replaceFragment(statisticsFragment));
        shoppingCart.setOnClickListener(view -> replaceFragment(shoppingFragment));
    }

    //Sets first fragment in the activity as the lessons fragment
    private void setInitialFragment(boolean verify) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!verify) {
            fragmentTransaction.add(R.id.frame, lessonsFragment);
        }
        else {
            fragmentTransaction.add(R.id.frame, statisticsFragment);
        }
        fragmentTransaction.commit();
    }

    //Replaces fragment currently set on activity to appropriate fragment passed to the function
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManagerReplace = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionReplace = fragmentManagerReplace.beginTransaction();
        fragmentTransactionReplace.replace(R.id.frame, fragment);
        fragmentTransactionReplace.commit();
    }

    //Overrides the back button normal functions to automatically set the lessons fragment
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        replaceFragment(lessonsFragment);
    }
}