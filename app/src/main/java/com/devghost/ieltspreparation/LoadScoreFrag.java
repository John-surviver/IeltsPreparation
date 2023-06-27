package com.devghost.ieltspreparation;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devghost.ieltspreparation.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.MessageFormat;
import java.util.Objects;

public class LoadScoreFrag extends Fragment {

    private static final String PREF_NAME = "UserPrefs";
    private static final String POINTS_KEY = "points";

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private String loggedEmail;
    private TextView points_tv;
    private ListenerRegistration listenerRegistration;
    private Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_score, container, false);


        db = FirebaseFirestore.getInstance();
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, 0);
        points_tv = view.findViewById(R.id.points_tv);
        context = requireContext();


        if (mAuth.getCurrentUser() != null) {
            loggedEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        }





        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            fetchPoints();
            registerPointsListener();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterPointsListener();
    }

    private void fetchPoints() {
        int cachedPoints = sharedPreferences.getInt(POINTS_KEY, 0);
        points_tv.setText(MessageFormat.format("{0}", cachedPoints));

        db.collection("Users").document(loggedEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("UserName");
                    String points = documentSnapshot.getString("points");
                    if (points != null) {
                        int pointsValue = Integer.parseInt(points);

                        UserModel userModel = new UserModel(name, pointsValue);
                        updatePoints(userModel);
                        cachePoints(pointsValue);


                    }
                }
            } else {
                showToast("Failed");
            }
        }).addOnFailureListener(e -> showToast("Failed"));
    }

    private void updatePoints(UserModel userModel) {
        int points = userModel.getPoints();
        points_tv.setText(MessageFormat.format("{0}", points));
    }

    private void cachePoints(int points) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(POINTS_KEY, points);
        editor.apply();
    }

    private void registerPointsListener() {
        listenerRegistration = db.collection("Users").document(loggedEmail).addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                showToast("Failed to fetch points");
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                String points = snapshot.getString("points");
                if (points != null) {
                    int pointsValue = Integer.parseInt(points);
                    UserModel userModel = new UserModel(snapshot.getString("UserName"), pointsValue);
                    updatePoints(userModel);
                    cachePoints(pointsValue);
                }
            }
        });
    }

    private void unregisterPointsListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
