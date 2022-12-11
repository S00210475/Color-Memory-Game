package com.example.colormemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button btnUp, btnDown, btnLeft, btnRight;
    TextView gameDetails;
    Vibrator v;
    boolean positionChange, atBase, allowVibration, gameStart = false;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    CountDownTimer countDownTimer, commenceSequence;
    List<String> sequence = new ArrayList<String>();
    int sequencePosition = 0, sequenceTime = 3000, sequenceInterval = 1000, score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.i("Test", "Initialization");
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        btnRight = findViewById(R.id.btnRight);
        btnLeft = findViewById(R.id.btnLeft);

        gameDetails = findViewById(R.id.gameDetails);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        allowVibration = getIntent().getBooleanExtra("allowVibration", true);
        //Commence game
        gameDetails.bringToFront();
        startGame();
    }
    public void startGame()
    {
        countDownTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                gameDetails.setText(String.valueOf(millisUntilFinished / 1000 + 1));
            }
            public void onFinish() {
                gameDetails.setText(" ");
                commenceSequence();
            }
        }.start();
    }
    public void commenceSequence()
    {
        commenceSequence = new CountDownTimer(sequenceTime,  sequenceInterval) {
            public void onTick(long millisUntilFinished) {
                setSequence();
            }
            public void onFinish() {
                sequence.forEach((s) -> Log.i("Test", s));
                sequencePosition = 0;
                gameStart = true;
                gameDetails.setText("Commence!");
            }
        }.start();
    }
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }
    /*
     * Called by the system every x millisecs when sensor changes
     */
    public void onSensorChanged(SensorEvent event) {
        // called byt the system every x ms
        if(gameStart)
        {
            clickValidation(event);
        }
    }
    public void clickValidation(SensorEvent event)
    {
        float x, y;
        x = Math.abs(event.values[0]); // get x value
        y = event.values[1];
        if(y < -2 && atBase)
        {
            colorClick(btnLeft);
            positionChange = true;
            validateAnswer("Left");
        }
        else if(y > 2 && atBase)
        {
            colorClick(btnRight);
            positionChange = true;
            validateAnswer("Right");
        }
        else if(x>9 && atBase)
        {
            colorClick(btnDown);
            positionChange = true;
            validateAnswer("Down");
        }
        else if(x<4 && atBase)
        {
            colorClick(btnUp);
            positionChange = true;
            validateAnswer("Up");
        }
        else if(x>=4 && x<=9 && y <=2 && y >=-2) //Checks if position is at base
        {
            positionChange = false;
            atBase = true;
        }

        if(positionChange && atBase)
        {
            if(allowVibration) {
                v.vibrate(100);
            }
            positionChange = false;
            atBase = false;
        }
    }
    public void colorClick(Button btn)
    {
            Log.i("Test", "Color Click");
            btn.performClick();
            btn.setPressed(true);
            btn.invalidate();
            btn.setPressed(false);
            btn.invalidate();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not using
    }

    //Game Start
    private void validateAnswer(String answer)
    {
        Log.i("Test", Integer.toString(sequencePosition));
        Log.i("Test", "Valid Answer: " + sequence.get(sequencePosition) + " || Answer Given: " + answer);
        if(answer == sequence.get(sequencePosition))
        {
            score++;
            gameDetails.setText("Score: " + score);
            sequencePosition++;
            if(sequence.size() == sequencePosition)
            {
                Log.i("Test", "Sequence if statement test: " + Integer.toString(sequencePosition) + " | " + Integer.toString(sequence.size()));
                sequence.clear();
                sequencePosition = 0;
                gameStart = false;
                sequenceTime = sequenceTime + 1000;
                Log.i("Test", Integer.toString(sequenceTime));
                if(sequenceTime == 10000)
                {
                    sequenceInterval = 500; //Speeds up the game
                }
                startGame();
            }
        }
        else {
            Log.i("Test", "Oof");
            Intent scorePage = new Intent(MainActivity.this, GameOverScreen.class);
            scorePage.putExtra("score", score);
            startActivity(scorePage);     // start the new page
            Log.i("Test", "Opening activity");
            finish();
        }
    }
    private void setSequence() {
            int r = getRandom(4);
            switch (r) {
                case 1:
                    colorClick(btnUp);
                    sequence.add("Up");
                    break;
                case 2:
                    colorClick(btnDown);
                    sequence.add("Down");
                    break;
                case 3:
                    colorClick(btnLeft);
                    sequence.add("Left");
                    break;
                case 4:
                    colorClick(btnRight);
                    sequence.add("Right");
                    break;
                default:
                    break;
            }   // end switch
    }
    private int getRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }
}