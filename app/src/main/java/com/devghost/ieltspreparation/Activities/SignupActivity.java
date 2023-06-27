package com.devghost.ieltspreparation.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.devghost.ieltspreparation.LoginOrSignUp;
import com.devghost.ieltspreparation.R;
import com.devghost.ieltspreparation.Models.UserList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    AutoCompleteTextView ed_email;
    EditText ed_username,ed_password;
    Button signUpbtn,login_btn;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar progressBar;
    LottieAnimationView lottieAnimationView,lottieAnimationView2;
    CardView cardView;
    TextView textView;
    UserList user;
    int POINTS =0;
    String points ="";
    String ad = "";
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //assign ids to views
        assignIds();
        //set onclick listener to continue button

        //check if the user already has an account
        authStateListener= firebaseAuth -> currentUser=firebaseAuth.getCurrentUser();

        if(mAuth.getCurrentUser()==null){
            Toast.makeText(this, "Login or Sign Up", Toast.LENGTH_SHORT).show();
        }
        else if (mAuth.getCurrentUser()!=null){
            Intent intent2 = new Intent(this,MainActivity.class);
            startActivity(intent2);
            Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();
            finish();
        }

        //create a new account
        signUpbtn.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(ed_username.getText().toString())
                    &&!TextUtils.isEmpty(ed_email.getText().toString())
                    &&!TextUtils.isEmpty(ed_password.getText().toString())){


                String name = ed_username.getText().toString();
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();

                user = new UserList(name,email,password);

                createNewAccount(name,email,password);
            }
            else{
                ed_email.setError("Empty");
                ed_username.setError("Empty");
                ed_password.setError("Empty");
                ed_email.requestFocus();
                ed_password.requestFocus();
                ed_username.requestFocus();
            }

        });


        login_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginOrSignUp.class);
            startActivity(intent);
        });
    }

    private void assignIds() {
        ed_email=findViewById(R.id.ed_inputEmailId_signUp);
        ed_username=findViewById(R.id.ed_username);
        ed_password=findViewById(R.id.ed_inputPasswordId_signUp);
        signUpbtn=findViewById(R.id.btn_continue);
        progressBar=findViewById(R.id.sign_up_progress);
        login_btn=findViewById(R.id.btn_login2);
        lottieAnimationView=findViewById(R.id.account_create_lottie);
        lottieAnimationView2=findViewById(R.id.animationView3);
        cardView=findViewById(R.id.cardView4);
        textView=findViewById(R.id.textView2);
        //assign ids to firebase
        firebaseAuth=FirebaseAuth.getInstance();
    }


    public void createNewAccount(String name,String email,String password){
        if(!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)){

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    lottieAnimationView2.setVisibility(View.GONE);
                    cardView.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    login_btn.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    currentUser=firebaseAuth.getCurrentUser();
                    assert currentUser!=null;
                    String userUid = currentUser.getUid();
                    points=String.valueOf(POINTS);

                    Map<String,Object> userObj = new HashMap<>();
                    userObj.put("userId",userUid);
                    userObj.put("FullDetails",user);
                    userObj.put("UserName",name);
                    userObj.put("points",points);
                    userObj.put("ad",ad);


                    db.collection("Users").document(email)
                            .set(userObj)
                            .addOnSuccessListener(unused -> {


                                int SPLASH_DISPLAY_LENGTH=4000;
                                new Handler().postDelayed(() -> {
                                    /* Create an Intent that will start the Menu-Activity. */
                                    Intent mainIntent = new Intent(SignupActivity.this,MainActivity.class);
                                    startActivity(mainIntent);
                                    SignupActivity.this.finish();
                                }, SPLASH_DISPLAY_LENGTH);




                            }).addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show());

                }else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }


}