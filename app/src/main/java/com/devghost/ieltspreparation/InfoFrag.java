package com.devghost.ieltspreparation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class InfoFrag extends Fragment {

    View view;
    CircularProgressBar progressBar1,progressBar2,progressBar3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info, container, false);

        progressBar1=view.findViewById(R.id.circularProgressBar);
        progressBar2=view.findViewById(R.id.circularProgressBar1);
        progressBar3=view.findViewById(R.id.circularProgressBar2);

        progressBar1.setProgress(50f);
        progressBar1.setProgressMax(100f);

        progressBar2.setProgress(80f);
        progressBar2.setProgressMax(100f);

        progressBar3.setProgress(30f);
        progressBar3.setProgressMax(100f);


        return view;
    }
}