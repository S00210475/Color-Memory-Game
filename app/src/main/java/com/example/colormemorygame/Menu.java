package com.example.colormemorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Menu extends AppCompatActivity {
    PlayMusicTask music = new PlayMusicTask(this);
    ImageView stopMusicIcon;
    ImageView stopVibrateIcon;
    boolean allowVibration = true;
    Button musicBtn;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
            //XML retrieval
        stopMusicIcon = findViewById(R.id.stopMusicIcon);
        musicBtn = findViewById(R.id.musicBtn);
        // To execute the AsyncTask:
        music.execute();
        db = new DatabaseHandler(this);
        //leaderboard initialization for first time download
        if(db.getTopFiveScores().size() == 0)
        {
            Log.i("Test", "Adding hiscores" );
            //Initialization of data
            db.addHiScore(new HiScore("20/11/2020", "Freddy", 2));
            db.addHiScore(new HiScore("20/11/2020", "Daniel", 3));
            db.addHiScore(new HiScore("20/11/2020", "ABC", 15));
            db.addHiScore(new HiScore("20/11/2020", "Hugh", 5));
            db.addHiScore(new HiScore("22/11/2020", "Geraldine", 13));
        }
    }

    public void musicSetting(View view) {
        if (music.isPlaying()) {
            music.stopMusic();
            stopMusicIcon.setVisibility(view.VISIBLE);
        } else {
            music.startMusic();
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