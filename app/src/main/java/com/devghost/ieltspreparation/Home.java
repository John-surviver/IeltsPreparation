package com.devghost.ieltspreparation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.devghost.ieltspreparation.Grammar.GrammarFrag;
import com.devghost.ieltspreparation.Grammar.GrammarList;
import com.devghost.ieltspreparation.Listening.ListeningMenu;
import com.devghost.ieltspreparation.Reading.ReadingMenu;
import com.devghost.ieltspreparation.Speaking.SpeakingMenu;
import com.devghost.ieltspreparation.Tips.TipsMenu;
import com.devghost.ieltspreparation.Writing.WritingMenu;

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
            @SuppressLint("ViewHolder") View myView = layoutInflater.inflate(R.layout.design_lay, viewGroup, false);
            ImageView pic = myView.findViewById(R.id.pic_id);
            TextView Title = myView.findViewById(R.id.title_tv);
            RelativeLayout linearLayout = myView.findViewById(R.id.secondLay);

            HashMap<String, String> hashMap = arrayList.get(position);
            String Pic = hashMap.get("pic");
            String title = hashMap.get("title");
            String tag = hashMap.get("tag");


            Title.setText(title);
            pic.setImageResource(Integer.parseInt(Objects.requireNonNull(Pic)));

            linearLayout.setOnClickListener(view1 -> {


                if("1".equals(tag)){
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new ReadingMenu());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("2".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new ListeningMenu());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("3".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new SpeakingMenu());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("4".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new WritingMenu());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("5".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new GrammarList());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("6".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new TipsMenu());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("7".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new IdomsAndPhrases());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if ("8".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new MockTest());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                else if ("9".equals(tag)) {
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new Vocabularies());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

                else if ("10".equals(tag)) {
                    GrammarFrag.webLink="file:///android_asset/ielts_score_overview.html";
                    FragmentManager fragment = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                    fragmentTransaction.replace(R.id.mainLay,new GrammarFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });


            return myView;
        }
    }


    private void createTable() {


        hashMap = new HashMap<>();
        hashMap.put("tag","2");
        hashMap.put("pic", String.valueOf(R.drawable.listening));
        hashMap.put("title", "Listening");
        arrayList.add(hashMap);



        hashMap = new HashMap<>();
        hashMap.put("tag","1");
        hashMap.put("pic", String.valueOf(R.drawable.read));
        hashMap.put("title", "Reading");
        arrayList.add(hashMap);



        hashMap = new HashMap<>();
        hashMap.put("tag","3");
        hashMap.put("pic", String.valueOf(R.drawable.conversation));
        hashMap.put("title", "Speaking");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("tag","4");
        hashMap.put("pic", String.valueOf(R.drawable.writing));
        hashMap.put("title", "Writing");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("tag","9");
        hashMap.put("pic", String.valueOf(R.drawable.dictionary));
        hashMap.put("title", "1500+ common\nVocabularies");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("tag","7");
        hashMap.put("pic", String.valueOf(R.drawable.language));
        hashMap.put("title", "Idioms and Phrases");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("tag","5");
        hashMap.put("pic", String.valueOf(R.drawable.education));
        hashMap.put("title", "Grammar");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("tag","6");
        hashMap.put("pic", String.valueOf(R.drawable.chat));
        hashMap.put("title", "Tips And Tricks");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("tag","8");
        hashMap.put("pic", String.valueOf(R.drawable.exam));
        hashMap.put("title", "Mock Test");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("tag","10");
        hashMap.put("pic", String.valueOf(R.drawable.score_overview));
        hashMap.put("title", "IELTS Band Score\nOverview");
        arrayList.add(hashMap);

    }



}