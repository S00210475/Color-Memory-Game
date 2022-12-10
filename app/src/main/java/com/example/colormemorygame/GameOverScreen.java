package com.example.colormemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
            //db.removeAllButTop();
            bottomScore = top5HiScores.get(top5HiScores.size() - 1);
            for (HiScore s:allScores ) {
                Log.i("Test", s.getPlayer_name() + ": " + Integer.toString(s.getScore()));
            }
            // hiScore contains the 5th highest score
            Log.i("Fifth Highest score: ", String.valueOf(bottomScore.getScore()) );
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
}