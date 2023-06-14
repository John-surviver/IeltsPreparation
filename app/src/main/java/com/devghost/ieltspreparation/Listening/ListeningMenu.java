package com.devghost.ieltspreparation.Listening;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.devghost.ieltspreparation.R;

public class ListeningMenu extends Fragment implements View.OnClickListener {

    View view;
    LinearLayout btn1,btn2,btn3,listening_menu_lay;
    LottieAnimationView lottieAnimationView;
    private AlertDialog internetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listening_menu, container, false);

        //assignIds
        assignIds();
        setClickAble();





        return view;

    }

    private void setClickAble() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    private void assignIds() {
        btn1=view.findViewById(R.id.basic_listening_btn);
        btn2=view.findViewById(R.id.intermediate_listening_btn);
        btn3=view.findViewById(R.id.advanced_listening_btn);
        listening_menu_lay=view.findViewById(R.id.listening_menu_lay);
        lottieAnimationView=view.findViewById(R.id.animationView1);

    }

    @Override
    public void onClick(View v) {
        if (!isInternetConnected()) {
            showNoInternetDialog();
            return;
        }

        if(v.getId()==R.id.basic_listening_btn){
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ListeningListOne());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId()==R.id.intermediate_listening_btn) {
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ListeningList2());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId()==R.id.advanced_listening_btn) {
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ListeningList3());
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

        listening_menu_lay.setVisibility(View.GONE);
        lottieAnimationView.setVisibility(View.VISIBLE);

        int SPLASH_DISPLAY_LENGTH = 6000;
        new Handler().postDelayed(() -> {
            lottieAnimationView.setVisibility(View.GONE);
            listening_menu_lay.setVisibility(View.VISIBLE);


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

