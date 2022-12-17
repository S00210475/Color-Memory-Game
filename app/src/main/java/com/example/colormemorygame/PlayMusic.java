package com.example.colormemorygame;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

// Async solution for music
class PlayMusicTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private MediaPlayer mediaPlayer;

    public PlayMusicTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mediaPlayer = MediaPlayer.create(context, R.raw.black_hole);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        return null;
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    public void startMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }
}