package com.example.colormemorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

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
            db.addHiScore(new HiScore("20/11/2022", "Freddy", 2));
            db.addHiScore(new HiScore("20/11/2022", "Daniel", 3));
            db.addHiScore(new HiScore("20/11/2022", "ABC", 15));
            db.addHiScore(new HiScore("20/11/2022", "Hugh", 1));
            db.addHiScore(new HiScore("22/11/2022", "Geraldine", 5));
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
    public void showLeaderboard(View view) {
        // inflate the layout of the popup window
        Log.i("Test", "Leaderboard method called");
        //Leaderboard setup
//==================================================
        String[] rows = new String[5];
        int counter = 0;
        for (HiScore score: db.getTopFiveScores()) {
            rows[counter] = Integer.toString(counter + 1) + ". Name: " + score.getPlayer_name() + "\n\n Score: " + Integer.toString(score.getScore()) + " Date: " + score.getGame_date();
            counter++;
        }
        ListView listViewItems = new ListView(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                R.layout.item_view, R.id.itemTextView, rows);

        listViewItems.setAdapter(arrayAdapter);
        AlertDialog alertDialogStores = new AlertDialog.Builder(this)
                .setView(listViewItems)
                .show();
//===================================================
    }
}