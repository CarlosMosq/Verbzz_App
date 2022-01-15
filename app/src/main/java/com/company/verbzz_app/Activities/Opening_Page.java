package com.company.verbzz_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.verbzz_app.R;

public class Opening_Page extends AppCompatActivity {

    ImageView honeycomb;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_page);

        honeycomb = findViewById(R.id.honeyCombBlank);
        title = findViewById(R.id.app_title_blank);

        //Loads the animation created in anim folder into the honeycomb picture and title at the opening page;
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition);
        honeycomb.startAnimation(animation);
        title.startAnimation(animation);

        //Plays the buzz sound audio when app is opened;
        MediaPlayer player = MediaPlayer.create(this, R.raw.bee);
        player.start();
        player.setOnCompletionListener(MediaPlayer::release);

        //Waits 5 seconds before jumping to next page;
        new Handler().postDelayed(() -> {
            Intent i = new Intent(Opening_Page.this, Authentication_Page.class);
            startActivity(i);
            finish();
        }, 5000);

    }
}