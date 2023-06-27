package com.devghost.ieltspreparation.Reading;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.devghost.ieltspreparation.FreeSample.FreeReadingList;
import com.devghost.ieltspreparation.R;

public class ReadingMenu extends Fragment implements View.OnClickListener {

    View view;
    CardView btn1,btn2,btn3,btn4;
    LinearLayout menu_lay;
    LottieAnimationView lottieAnimationView;
    private AlertDialog internetDialog;
    ImageView imageView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.menu_frag, container, false);

        //assignIds
        assignIds();
        //set Onclick
        setClickAble();

        startRotationAnimation(btn1);
        startRotationAnimation(btn2);
        startRotationAnimation(btn3);
        startRotationAnimation(btn4);

        imageView.setImageResource(R.drawable.free_reading);
        textView.setText(R.string.free_reading);




        return view;
    }
    private void startRotationAnimation(View view) {
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotationAnimator.setDuration(2000); // Duration of 2 seconds

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotationAnimator);
        animatorSet.start();

        // Stop the animation after 2 seconds
        new Handler().postDelayed(() -> {
            animatorSet.cancel();
            view.setRotation(0f); // Set rotation to 0 to return to the normal position
        }, 2000);
    }

    private void setClickAble() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    private void assignIds() {
        btn1=view.findViewById(R.id.basic_listening_btn);
        btn2=view.findViewById(R.id.intermediate_listening_btn);
        btn3=view.findViewById(R.id.advanced_listening_btn);
        btn4=view.findViewById(R.id.extra_btn);
        menu_lay =view.findViewById(R.id.listening_menu_lay);
        lottieAnimationView=view.findViewById(R.id.animationView1);
        imageView=view.findViewById(R.id.extra_iv);
        textView=view.findViewById(R.id.extra_tv);

    }

    @Override
    public void onClick(View v) {
        if (!isInternetConnected()) {
            showNoInternetDialog();
            return;
        }

        if(v.getId()==R.id.basic_listening_btn){
            ReadingList1.LOAD_LINK="https://codemind.live/apps/ielts/reading/beginner/get.php";
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ReadingList1());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId()==R.id.intermediate_listening_btn) {
            ReadingList1.LOAD_LINK="https://codemind.live/apps/ielts/reading/intermediate/get.php";
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ReadingList1());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId()==R.id.advanced_listening_btn) {
            ReadingList1.LOAD_LINK="https://codemind.live/apps/ielts/reading/advanced/get.php";
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ReadingList1());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        else if (v.getId()==R.id.extra_btn) {

            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new FreeReadingList());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }


    private void showNoInternetDialog() {

        menu_lay.setVisibility(View.GONE);
        lottieAnimationView.setVisibility(View.VISIBLE);

        int SPLASH_DISPLAY_LENGTH = 6000;
        new Handler().postDelayed(() -> {
            lottieAnimationView.setVisibility(View.GONE);
            menu_lay.setVisibility(View.VISIBLE);


            if (internetDialog == null || !internetDialog.isShowing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("No Internet Connection")
                        .setMessage("Please check your internet connection and try again.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                internetDialog = builder.create();
                internetDialog.show();
            }

        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
        }
    }
}