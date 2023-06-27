package com.devghost.ieltspreparation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devghost.ieltspreparation.Activities.MainActivity;
import com.devghost.ieltspreparation.Activities.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginOrSignUp extends AppCompatActivity implements View.OnClickListener {

    AutoCompleteTextView ed_email;
    AutoCompleteTextView ed_password;
    Button login, signUp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    LinearLayout login_lottie_lay;
    ConstraintLayout login_lay;
    ProgressBar progressBar;

    private static final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);

        assignIds();

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    private void assignIds() {
        login = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.btn_signUp);
        ed_email = findViewById(R.id.ed_inputEmailId);
        ed_password = findViewById(R.id.ed_inputPasswordId);
        login_lottie_lay = findViewById(R.id.login_lottie_lay);
        login_lay = findViewById(R.id.main_login_lay);
        progressBar = findViewById(R.id.progress_bar);
    }
    @Override
    public void onClick(View v) {
        String email = ed_email.getText().toString().trim();
        String password = ed_password.getText().toString().trim();

        if (v.getId() == R.id.btn_login) {
            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                ed_email.setError("Empty");
                ed_password.setError("Empty");
                ed_email.requestFocus();
                ed_password.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        login_lay.setVisibility(View.GONE);
                        login_lottie_lay.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() -> {
                            Intent mainIntent = new Intent(this, MainActivity.class);
                            startActivity(mainIntent);
                            LoginOrSignUp.this.finish();
                        }, SPLASH_DISPLAY_LENGTH);
                    } else {
                        Toast.makeText(LoginOrSignUp.this, "No User Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (v.getId() == R.id.btn_signUp) {
            startActivity(new Intent(LoginOrSignUp.this, SignupActivity.class));
        }

    }
}