package com.example.colormemorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameOverScreen extends AppCompatActivity {

    HiScore score = new HiScore();
    HiScore bottomScore; //Lowest leaderboard score
    EditText nameInput;
    TextView scoreTv;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();

        //Importing score from game page
        score.setScore(getIntent().getIntExtra("score", -1));
        //XML
        scoreTv = findViewById(R.id.scoreTv);
        nameInput = findViewById(R.id.nameInput);
        //Database Setup
        db = new DatabaseHandler(this);

        scoreTv.setText(Integer.toString(score.getScore()));

        List<HiScore> allScores = db.getAllHiScores();
        List<HiScore> top5HiScores = db.getTopFiveScores();
        try {
            Log.i("Test", "Checking bottomscore" );
            bottomScore = top5HiScores.get(top5HiScores.size() - 1);
            // hiScore contains the 5th highest score
            Log.i("Test", "Fifth Highest score:" + String.valueOf(bottomScore.getScore()) );
            // simple test to add a hi score
            // if 5th highest score < myCurrentScore, then insert new score
                if (bottomScore.getScore() < score.getScore()) {
                    Log.i("Test", "Name field appear");
                    nameInput.setVisibility(GameOverScreen.this.nameInput.VISIBLE);
                }
        }
        catch (Error err)
        {
            //If there are no entries in the database
            Log.i("Test", "Catch method called");
        }

    }
    public void closeWindow(View view) {
        Log.i("Test", "Close Window Method Called");
        String name = nameInput.getText().toString();
        if(name == null)
        {
            name = "Anonymous";
        }
        if (bottomScore.getScore() < score.getScore()) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            db.deleteHiScore(bottomScore);
            db.addHiScore(new HiScore( formatter.format(date), name, score.getScore()));
        }
        finish();
    }
    public void showLeaderboard(View view) {
        // inflate the layout of the popup window
        Log.i("Test", "Leaderboard method called");
        //Leaderboard setup
//==================================================
        String[] rows = new String[5];
        String[] headers = {"Rank","Name", "Score", "Date"};
        int counter = 0;
        for (HiScore score: db.getTopFiveScores()) {
            rows[counter] = Integer.toString(counter + 1) + ". Name: " + score.getPlayer_name() + " | Score: " + Integer.toString(score.getScore()) + " | Date: " + score.getGame_date();
            counter++;
        }
        ListView listViewItems = new ListView(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                R.layout.item_view, R.id.itemTextView, rows);

        listViewItems.setAdapter(arrayAdapter);
        AlertDialog alertDialogStores = new AlertDialog.Builder(this)
                .setView(listViewItems)
                .setTitle("Leaderboard")
                .show();
//===================================================
    }
}