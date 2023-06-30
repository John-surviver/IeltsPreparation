package com.devghost.ieltspreparation.Activities;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.devghost.ieltspreparation.MyApplication;
import com.devghost.ieltspreparation.R;

/** Splash Activity that inflates splash activity xml. */
public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SplashActivity";


    private static final long COUNTER_TIME = 3;

    private long secondsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Create a timer so the SplashActivity will be displayed for a fixed amount of time.
        createTimer();
    }

    /**
     * Create the countdown timer, which counts down to zero and show the app open ad.
     */
    private void createTimer() {
      //  final TextView counterTextView = findViewById(R.id.timer);

        CountDownTimer countDownTimer =
                new CountDownTimer(SplashActivity.COUNTER_TIME * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        secondsRemaining = ((millisUntilFinished / 1000) + 1);
                       // counterTextView.setText("Welcome: " + secondsRemaining);
                    }

                    @Override
                    public void onFinish() {
                        secondsRemaining = 0;
                        //  counterTextView.setText("Done.");

                        Application application = getApplication();

                        // If the application is not an instance of MyApplication, log an error message and
                        // start the MainActivity without showing the app open ad.
                        if (!(application instanceof MyApplication)) {
                            Log.e(LOG_TAG, "Failed to cast application to MyApplication.");
                            startMainActivity();
                            return;
                        }

                        // Show the app open ad.
                        ((MyApplication) application)
                                .showAdIfAvailable(
                                        SplashActivity.this,
                                        () -> startMainActivity());
                    }
                };
        countDownTimer.start();
    }

    /** Start the MainActivity. */
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
