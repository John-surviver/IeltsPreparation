package com.devghost.ieltspreparation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.devghost.ieltspreparation.Activities.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class MockTest extends Fragment {

    View view;
    LinearLayout coin_lay,money;
    public static int coin = 0;
    int POINTS = 0;
    int MOCK = 0;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private int totalPoints,totalMock;

    private String loggedEmail;

    LottieAnimationView pay;
    CardView a,b;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mock_test, container, false);

        assignIds();

        if (firebaseAuth.getCurrentUser() == null) {
            AlertDialog.Builder alertdialogbuilder;
            alertdialogbuilder = new AlertDialog.Builder(requireContext());
            alertdialogbuilder.setIcon(R.drawable.ic_launcher_foreground);
            alertdialogbuilder.setTitle("Login Please");
            alertdialogbuilder.setMessage("For Mock Test Login Please");
            alertdialogbuilder.setCancelable(false);

            alertdialogbuilder.setPositiveButton("yes", (dialogInterface, i) -> startActivity(new Intent(requireContext(), SignupActivity.class)));


            AlertDialog alertDialog = alertdialogbuilder.create();
            alertDialog.show();
        }

        else {
            loggedEmail = currentUser.getEmail();
            getPoints();
        }


        coin_lay.setOnClickListener(v -> {
            if (currentUser != null) {
                if(POINTS<5000){
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Not enough Coins")
                            .setMessage("You don't have enough coins")
                            .create()
                            .show();
                }
                else {
                    POINTS -= 5000;
                    MOCK+=1;
                    updatePoints();

                    pay.setVisibility(View.VISIBLE);
                    money.setVisibility(View.GONE);
                    coin_lay.setVisibility(View.GONE);
                    a.setVisibility(View.GONE);
                    b.setVisibility(View.GONE);

                    int SPLASH_DISPLAY_LENGTH = 4000;
                    new Handler().postDelayed(() -> {
                        FragmentManager fragment = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                        fragmentTransaction.replace(R.id.mainLay,new BoughtByCoins());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }, SPLASH_DISPLAY_LENGTH);


                }

            }
        });

        money.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Coming Soon")
                    .setMessage("Buy with Coins for now")
                    .create()
                    .show();
        });

        return view;
    }

    private void assignIds() {
        coin_lay =view.findViewById(R.id.buy_with_coin);
        money=view.findViewById(R.id.buy_with_money);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        pay=view.findViewById(R.id.pay_anim);
        a=view.findViewById(R.id.cardView7);
        b=view.findViewById(R.id.cardView8);
    }

    private void getPoints() {
        // Check if points are already cached
        if (totalPoints != 0) {
            POINTS += totalPoints;
            return;
        }

        db.collection("Users").document(loggedEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String pointsString = documentSnapshot.getString("points");
                    String mockString = documentSnapshot.getString("mock");

                    totalPoints = Integer.parseInt(pointsString);
                    totalMock = Integer.parseInt(mockString);
                    POINTS += totalPoints;
                    MOCK+=totalMock;
                }
            } else {
                Toast.makeText(getActivity(), "Failed to retrieve points", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to retrieve points", Toast.LENGTH_SHORT).show());
    }
    private void updatePoints() {
        String updatedPoints = String.valueOf(POINTS);
        String updatedMock = String.valueOf(MOCK);
        Map<String, Object> data = new HashMap<>();
        data.put("points", updatedPoints);
        data.put("mock", updatedMock);

        DocumentReference userRef = db.collection("Users").document(loggedEmail);

        WriteBatch batch = db.batch();
        batch.update(userRef, data);
        batch.commit()
                .addOnSuccessListener(unused -> {
                    totalPoints = POINTS; // Update the cached points
                    Toast.makeText(getActivity(), "Mock Added " , Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update points", Toast.LENGTH_SHORT).show());
    }
}