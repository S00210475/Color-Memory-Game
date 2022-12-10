package com.example.colormemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Menu extends AppCompatActivity {
    MediaPlayer music;
    ImageView stopMusicIcon;
    ImageView stopVibrateIcon;
    boolean allowVibration = true;
    Button musicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
            music = MediaPlayer.create(this, R.raw.mischievous);
            //XML retrieval
        stopMusicIcon = findViewById(R.id.stopMusicIcon);
        musicBtn = findViewById(R.id.musicBtn);
        //music.start(); //Plays music on startup
    }

    public void musicSetting(View view) {
        if (music.isPlaying()) {
            music.pause();
            stopMusicIcon.setVisibility(view.VISIBLE);
        } else {
            music.getDuration();
            music.start();
            stopMusicIcon.setVisibility(view.INVISIBLE);
        }
    }
    public void vibrationSetting(View view) {
        stopVibrateIcon = findViewById(R.id.stopPhoneIcon);
        if(allowVibration)
        {
            allowVibration = false;
            stopVibrateIcon.setVisibility(view.VISIBLE);
        }
        else{
            allowVibration = true;
            stopVibrateIcon.setVisibility(view.INVISIBLE);
        }
    }
    public void gameStart(View view) {
        Intent gamePage = new Intent(view.getContext(), MainActivity.class);
        gamePage.putExtra("allowVibration", allowVibration);
        startActivity(gamePage);     // start the new page
    }
}