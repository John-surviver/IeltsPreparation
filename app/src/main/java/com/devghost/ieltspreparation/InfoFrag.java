package com.devghost.ieltspreparation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.devghost.ieltspreparation.Models.DatabaseHelper;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.MessageFormat;

public class InfoFrag extends Fragment implements View.OnClickListener {

    View view;
    CircularProgressBar progressBar1,progressBar2,progressBar3,progressBar4,progressBar5;
    TextView textView;
    TextView p1,p2,p3,p4,p5;
    DatabaseHelper databaseHelper;
    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }


    //-------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info, container, false);

        assignIds();
        loadProgress();


        return view;
    }

    private void assignIds() {
        progressBar1=view.findViewById(R.id.circularProgressBar);
        progressBar2=view.findViewById(R.id.circularProgressBar1);
        progressBar3=view.findViewById(R.id.circularProgressBar2);
        progressBar4=view.findViewById(R.id.circularProgressBar4);
        progressBar5=view.findViewById(R.id.circularProgressBar5);
        textView=view.findViewById(R.id.textView6);
        p1=view.findViewById(R.id.p1);
        p2=view.findViewById(R.id.p2);
        p3=view.findViewById(R.id.p3);
        p4=view.findViewById(R.id.p4);
        p5=view.findViewById(R.id.p5);

    }

    private void loadProgress() {
        loadScores();
    }

    private void loadScores() {
        int[] scores = databaseHelper.getScores();

        int score_1 = (scores[0]); //reading
        int score_2 = (scores[1]); //listening
        int score_3 = (scores[2]); //speaking
        int score_4 = (scores[3]); //writing
        int score_5 = (scores[4]); //grammar


        if(score_1>100){
            score_1=100;
            p1.setText(MessageFormat.format("{0}%", score_1));
        }
        else {
            p1.setText(MessageFormat.format("{0}%", score_1));
        }
        if(score_2>100){
            score_2=100;
            p2.setText(MessageFormat.format("{0}%", score_2)); //listening
        }
        else {
            p2.setText(MessageFormat.format("{0}%", score_2)); //listening
        }
       if(score_3>100){
           score_3=100;
           p3.setText(MessageFormat.format("{0}%", score_3));
       }
       if(score_4>100){
           score_4=100;
           p4.setText(MessageFormat.format("{0}%", score_4));  //writing
       }
       else {
           p4.setText(MessageFormat.format("{0}%", score_4));  //writing
       }
       if(score_5>100){
           score_5=100;
           p5.setText(MessageFormat.format("{0}%", score_5)); //grammar
       }
       else {
           p5.setText(MessageFormat.format("{0}%", score_5)); //grammar
       }



        p5.setText(MessageFormat.format("{0}%", score_5)); //grammar

        float score1 = Float.parseFloat(String.valueOf(score_1));
        float score2 = Float.parseFloat(String.valueOf(score_2));
        float score3 = Float.parseFloat(String.valueOf(score_3));
        float score4 = Float.parseFloat(String.valueOf(score_4));
        float score5 = Float.parseFloat(String.valueOf(score_5));

        progressBar1.setProgress(score1);
        progressBar1.setProgressMax(100);

        progressBar2.setProgress(score2);
        progressBar2.setProgressMax(100);

        progressBar3.setProgress(score3);
        progressBar3.setProgressMax(100);

        progressBar4.setProgress(score4);
        progressBar4.setProgressMax(100);

        progressBar5.setProgress(score5);
        progressBar5.setProgressMax(100);
    }

    @Override
    public void onClick(View v) {

        }
}