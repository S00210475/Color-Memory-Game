package com.example.colormemorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

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

    public void showLeaderboard(View view) {
        // inflate the layout of the popup window
        Log.i("Test", "Leaderboard method called");
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.leaderboard_popup, null);
        Log.i("Test", "Test2");
        //Leaderboard setup
        DatabaseHandler db = new DatabaseHandler(this);
        Log.i("Test", "Test3");
//==================================================
        String[] rows = new String[5];
        int counter = 0;
        for (HiScore score: db.getTopFiveScores()) {
            rows[counter] = Integer.toString(counter + 1) + ". " + score.getPlayer_name() + " | " + Integer.toString(score.getScore()) + " | " + score.getGame_date();
            counter++;
        }
        ListView listViewItems = new ListView(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                R.layout.item_view, R.id.itemTextView, rows);
        Log.i("Test", "Test4");
        listViewItems.setAdapter(arrayAdapter);
        AlertDialog alertDialogStores = new AlertDialog.Builder(this)
                .setView(listViewItems)
                .setTitle("Leaderboard")
                .show();
//===================================================
        // create the popup window

    }
}