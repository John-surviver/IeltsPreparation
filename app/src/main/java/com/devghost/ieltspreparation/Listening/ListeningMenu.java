package com.devghost.ieltspreparation.Listening;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.devghost.ieltspreparation.R;

public class ListeningMenu extends Fragment implements View.OnClickListener {

    View view;
    LinearLayout btn1,btn2,btn3;

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
    }

    @Override
    public void onClick(View v) {
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
}

