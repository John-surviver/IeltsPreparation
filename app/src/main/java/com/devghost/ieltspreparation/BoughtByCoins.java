package com.devghost.ieltspreparation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class BoughtByCoins extends Fragment {

    View view;
    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bought_by_coins, container, false);

        btn=view.findViewById(R.id.clickToMail);

        btn.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Please select Gmail app or Email us", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"analysisghost8@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Mail for Mock");
            i.putExtra(Intent.EXTRA_TEXT   , "write your mail here");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        });


        return view;
    }
}