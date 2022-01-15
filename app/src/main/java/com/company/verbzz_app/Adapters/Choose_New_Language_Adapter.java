package com.company.verbzz_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.company.verbzz_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Choose_New_Language_Adapter extends RecyclerView.Adapter<Choose_New_Language_Adapter.FlagsViewHolder> {

    ArrayList<String> languages;
    Context context;

    public Choose_New_Language_Adapter(ArrayList<String> languages, Context context) {
        this.languages = languages;
        this.context = context;
    }

    @NonNull
    @Override
    public FlagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_flags_change_language, parent, false);

        return new FlagsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlagsViewHolder holder, int position) {
        if(languages.get(position).equals("English")) {
            holder.flag.setImageResource(R.drawable.english_language);
        }
        else if(languages.get(position).equals("French")) {
            holder.flag.setImageResource(R.drawable.french_language);
        }
        else {
            holder.flag.setImageResource(R.drawable.non_chosen_language);
        }

        holder.cardView.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            assert user != null;
            String userUID = user.getUid();
            reference
                    .child("Languages")
                    .child("Current Language")
                    .child(userUID)
                    .child("Current Language")
                    .setValue(languages.get(position));

            //check how to close the drawer from here and update language
        });

    }

    @Override
    public int getItemCount() {
        return languages.size();
    }


    public static class FlagsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView flag;
        private final CardView cardView;

        public FlagsViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.flagView);
            cardView = itemView.findViewById(R.id.languageFlag);
        }
    }


}
