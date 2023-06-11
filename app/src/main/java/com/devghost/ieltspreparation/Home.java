package com.devghost.ieltspreparation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Home extends Fragment {

    View view;
    GridView gridView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);


        gridView = view.findViewById(R.id.gridView);

        if (listAdapter == null) {
            createTable();
            listAdapter = new ListAdapter();
        }

        gridView.setAdapter(listAdapter);


        return view;
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.design_lay, viewGroup, false);
            ImageView pic = myView.findViewById(R.id.pic_id);
            TextView Title = myView.findViewById(R.id.title_tv);
            LinearLayout linearLayout = myView.findViewById(R.id.secondLay);

            HashMap<String, String> hashMap = arrayList.get(position);
            String Pic = hashMap.get("pic");
            String title = hashMap.get("title");


            Title.setText(title);
            pic.setImageResource(Integer.parseInt(Objects.requireNonNull(Pic)));

            linearLayout.setOnClickListener(view1 -> {



            });


            return myView;
        }
    }


    private void createTable() {
        hashMap = new HashMap<>();
        hashMap.put("pic", String.valueOf(R.drawable.read));
        hashMap.put("title", "Reading");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("pic", String.valueOf(R.drawable.listening));
        hashMap.put("title", "Listening");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("pic", String.valueOf(R.drawable.conversation));
        hashMap.put("title", "Speaking");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("pic", String.valueOf(R.drawable.writing));
        hashMap.put("title", "Writing");
        arrayList.add(hashMap);
    }



}