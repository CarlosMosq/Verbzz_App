package com.company.verbzz_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.Adapters.StatsAdapter;
import com.company.verbzz_app.Classes.Stats;
import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StatisticsFragment extends Fragment {

    private StatsAdapter statsAdapter;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Stats> list;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        assert user != null;
        String userUID = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Languages").child("Scores").child(userUID);

        RecyclerView recyclerView = view.findViewById(R.id.statsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Stats stats = dataSnapshot.getValue(Stats.class);
                    list.add(stats);
                }
                Collections.reverse(list);
                statsAdapter = new StatsAdapter(view.getContext(), list);
                recyclerView.setAdapter(statsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}