package com.devghost.ieltspreparation.FreeSample;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devghost.ieltspreparation.R;

import java.io.IOException;
import java.util.Locale;


public class FreeListeningLoad extends Fragment {

    View view;
    MediaPlayer mediaPlayer;
    ImageButton playbtn;
    SeekBar seekBar;
    private boolean isSeeking = false;
    TextView time_remaining,total_time,song_title;
    public static String AUDIO_URL="";
    public static String TITLE="";
    public static int ID = 0;

    ProgressBar progressBar,progressBar2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_free_listening_load, container, false);

        assignIds();
        mediaPlayerCheck();
        playAudio();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeeking = false;
            }
        });

        //what happens when audio ends
        mediaPlayer.setOnCompletionListener(mp -> {
            releaseMediaPlayer();
            seekBar.setProgress(0);
            playbtn.setImageResource(R.drawable.ic_play);
        });

        song_title.setText(TITLE);

        return view;
    }

    private void mediaPlayerCheck() {
        //audio player check
        mediaPlayer = new MediaPlayer();
        playbtn.setOnClickListener(v -> {

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                playbtn.setImageResource(R.drawable.ic_play);
                pauseAudio();
            } else {
                playbtn.setImageResource(R.drawable.pause);
                mediaPlayer.start();
            }

        });
    }

    private void assignIds() {
        playbtn=view.findViewById(R.id.play_pause_button2);
        seekBar=view.findViewById(R.id.seekbar2);
        time_remaining=view.findViewById(R.id.time_remaining2);
        total_time=view.findViewById(R.id.total_time2);
        song_title=view.findViewById(R.id.song_title2);
        seekBar=view.findViewById(R.id.seekbar2);
        progressBar=view.findViewById(R.id.loading_progressbar);
        progressBar2=view.findViewById(R.id.audio_progressbar);
    }

    private int lastPlayedPosition = 0; // Variable to store the last played position

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            lastPlayedPosition = mediaPlayer.getCurrentPosition(); // Store the current position
        }
    }

    private void playAudio() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());

            mediaPlayer.setDataSource(AUDIO_URL);
            mediaPlayer.prepareAsync();

            showLoadingProgressBar();
            progressBar2.setVisibility(View.VISIBLE);

            mediaPlayer.setOnPreparedListener(mp -> {
              //  Toast.makeText(requireContext(), "Playing now", Toast.LENGTH_SHORT).show();
                mediaPlayer.seekTo(lastPlayedPosition);

               // hideLoadingProgressBar();
                progressBar2.setVisibility(View.INVISIBLE);
                updateSeekBar();
            });

            mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
                int progress = (int) ((float) percent / 100 * progressBar.getMax());
                progressBar.setProgress(progress);
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading audio", Toast.LENGTH_SHORT).show();
        }
    }




    private void updateSeekBar() {
        if (mediaPlayer != null) {
            seekBar.setMax(mediaPlayer.getDuration());

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (!isSeeking && mediaPlayer != null && mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                    seekBar.postDelayed(this, 1000);

                    // Start the timer and update the TextView
                    updateTimer();
                }
            };

            seekBar.postDelayed(runnable, 0);
        }
    }

    private void updateTimer() {
        int currentPosition = mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
        int totalDuration = mediaPlayer != null ? mediaPlayer.getDuration() : 0;

        // Convert milliseconds to minutes and seconds
        int minutes = currentPosition / (1000 * 60);
        int seconds = (currentPosition / 1000) % 60;
        String currentPositionStr = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);


        // Update the TextView with the current position
// Convert milliseconds to minutes and seconds for total duration
        int totalMinutes = totalDuration / (1000 * 60);
        int totalSeconds = (totalDuration / 1000) % 60;
        String totalDurationStr = String.format(Locale.getDefault(), "%02d:%02d", totalMinutes, totalSeconds);

        // Update the TextViews with the current position and total duration
        time_remaining.setText(currentPositionStr);
        total_time.setText(totalDurationStr);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        releaseMediaPlayer();
    }

    private void showLoadingProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


}