package com.devghost.ieltspreparation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.devghost.ieltspreparation.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Remove title
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int SPLASH_DISPLAY_LENGTH = 4000;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
