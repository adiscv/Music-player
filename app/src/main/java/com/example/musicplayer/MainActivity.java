package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnPlayPause, btnNext, btnPrevious;
    private TextView songTitle, songDuration;
    private ImageView songCover;
    private SeekBar songProgress;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    private int[] songs = {R.raw.dilemma, R.raw.forever_young, R.raw.the_way_i_are};
    private int currentSongIndex = 0;
    private boolean isPlaying = false;

    private int[] songCovers = {R.drawable.dilemma, R.drawable.forever_young, R.drawable.the_way_i_are};
    private String[] songTitles = {"Dilemma", "Forever Young", "The Way I Are"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayPause = findViewById(R.id.btn_play_pause);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        songTitle = findViewById(R.id.song_title);
        songCover = findViewById(R.id.song_cover);
        songProgress = findViewById(R.id.song_progress);
        songDuration = findViewById(R.id.song_duration);

        setupMediaPlayer();

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseSong();
                } else {
                    playSong();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSong();
            }
        });

        songProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);
        mediaPlayer.setOnCompletionListener(mp -> nextSong());
        songProgress.setMax(mediaPlayer.getDuration());
        updateUI();
    }

    private void updateUI() {
        songTitle.setText(songTitles[currentSongIndex]);
        songCover.setImageResource(songCovers[currentSongIndex]);
        songProgress.setProgress(0);
        updateSeekBar();
    }

    private void playSong() {
        mediaPlayer.start();
        isPlaying = true;
        btnPlayPause.setText("Pause");
        updateSeekBar();
    }

    private void pauseSong() {
        mediaPlayer.pause();
        isPlaying = false;
        btnPlayPause.setText("Play");
    }

    private void nextSong() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        currentSongIndex = (currentSongIndex + 1) % songs.length;
        setupMediaPlayer();
        playSong();
    }

    private void previousSong() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        currentSongIndex = (currentSongIndex - 1 + songs.length) % songs.length;
        setupMediaPlayer();
        playSong();
    }

    private void updateSeekBar() {
        songProgress.setProgress(mediaPlayer.getCurrentPosition());
        songDuration.setText(formatTime(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(this::updateSeekBar, 1000);
        }
    }

    private String formatTime(int milliseconds) {
        int minutes = milliseconds / 60000;
        int seconds = (milliseconds % 60000) / 1000;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}