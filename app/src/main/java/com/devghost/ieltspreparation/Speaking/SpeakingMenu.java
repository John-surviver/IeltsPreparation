package com.devghost.ieltspreparation.Speaking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.devghost.ieltspreparation.R;
import com.devghost.ieltspreparation.Writing.WritingList1;
import com.devghost.ieltspreparation.Writing.WritingList2;
import com.devghost.ieltspreparation.Writing.WritingList3;


public class SpeakingMenu extends Fragment implements View.OnClickListener {

    View view;
    LinearLayout beginner,intermediate,advanced;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_writing_menu, container, false);

        beginner=view.findViewById(R.id.basic_writing_btn);
        intermediate=view.findViewById(R.id.intermediate_writing_btn);
        advanced=view.findViewById(R.id.advanced_writing_btn);

        beginner.setOnClickListener(this);
        intermediate.setOnClickListener(this);
        advanced.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.basic_writing_btn){
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new Speaking1());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(v.getId()==R.id.intermediate_writing_btn) {
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new Speaking2());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId()==R.id.advanced_writing_btn){
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new Speaking3());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}