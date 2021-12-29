package com.company.verbzz_app.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.company.verbzz_app.Fragments.LessonsFragment;
import com.company.verbzz_app.R;
import com.company.verbzz_app.Fragments.ShoppingFragment;
import com.company.verbzz_app.Fragments.StatisticsFragment;

public class MainActivity extends AppCompatActivity {

    Button homeButton;
    Button statistics;
    Button shoppingCart;
    private final LessonsFragment lessonsFragment = new LessonsFragment();
    private final StatisticsFragment statisticsFragment = new StatisticsFragment();
    private final ShoppingFragment shoppingFragment = new ShoppingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeButton = findViewById(R.id.homeButton);
        statistics = findViewById(R.id.statisticsButton);
        shoppingCart = findViewById(R.id.shoppingCart);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, lessonsFragment);
        fragmentTransaction.commit();

        homeButton.setOnClickListener(view -> replaceFragment(lessonsFragment));
        statistics.setOnClickListener(view -> replaceFragment(statisticsFragment));
        shoppingCart.setOnClickListener(view -> replaceFragment(shoppingFragment));
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManagerReplace = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionReplace = fragmentManagerReplace.beginTransaction();
        fragmentTransactionReplace.replace(R.id.frame, fragment);
        fragmentTransactionReplace.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        replaceFragment(lessonsFragment);
    }
}