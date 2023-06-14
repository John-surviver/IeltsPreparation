package com.devghost.ieltspreparation.Tips;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.devghost.ieltspreparation.Grammar.GrammarFrag;
import com.devghost.ieltspreparation.Grammar.GrammarList;
import com.devghost.ieltspreparation.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TipsMenu extends Fragment {

    View view;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    myAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tips_menu, container, false);

        listView=view.findViewById(R.id.tips_list);

        if (listAdapter == null) {
            createTable();
            listAdapter = new myAdapter();
        }
        listView.setAdapter(listAdapter);



        return view;
    }

    private class myAdapter extends BaseAdapter {

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
            View myView = layoutInflater.inflate(R.layout.list_design, viewGroup, false);
            TextView title = myView.findViewById(R.id.item_title);
            LinearLayout linearLayout = myView.findViewById(R.id.list_lay);

            HashMap<String, String> hashMap = arrayList.get(position);
            String Title = hashMap.get("title");
            String Link = hashMap.get("link");

            title.setText(Title);



            linearLayout.setOnClickListener(view1 -> {

                GrammarFrag.webLink=Link;

                FragmentManager fragment = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay,new GrammarFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            });


            return myView;
        }
    }

    private void createTable() {
        hashMap = new HashMap<>();
        hashMap.put("title", "Graph Writing");
        hashMap.put("link","file:///android_asset/graph_writing.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Letter Writing");
        hashMap.put("link","file:///android_asset/letter_writing.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Essay Writing");
        hashMap.put("link","file:///android_asset/essay_writing.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Reading Tips");
        hashMap.put("link","file:///android_asset/reading.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Listening Tips");
        hashMap.put("link","file:///android_asset/listening.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Speaking Tips");
        hashMap.put("link","file:///android_asset/speaking.html");
        arrayList.add(hashMap);


    }
}