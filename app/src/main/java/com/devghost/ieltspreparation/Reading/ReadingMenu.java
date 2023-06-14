package com.devghost.ieltspreparation.Reading;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.devghost.ieltspreparation.Listening.ListeningList2;
import com.devghost.ieltspreparation.Listening.ListeningList3;
import com.devghost.ieltspreparation.Listening.ListeningListOne;
import com.devghost.ieltspreparation.R;

public class ReadingMenu extends Fragment implements View.OnClickListener {

    View view;
    LinearLayout btn1, btn2, btn3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reading_menu, container, false);

        //assignIds
        assignIds();
        //set Onclick
        setClickAble();

        return view;
    }

    private void setClickAble() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    private void assignIds() {
        btn1 = view.findViewById(R.id.basic_reading_btn);
        btn2 = view.findViewById(R.id.intermediate_reading_btn);
        btn3 = view.findViewById(R.id.advanced_reading_btn);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.basic_reading_btn){
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ReadingList1());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId()==R.id.intermediate_reading_btn) {
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ReadingList2());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId()==R.id.advanced_reading_btn) {
            FragmentManager fragment = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragment.beginTransaction();
            fragmentTransaction.replace(R.id.mainLay,new ReadingList3());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}