package com.devghost.ieltspreparation.Listening;

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

import com.devghost.ieltspreparation.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListeningList2 extends Fragment {

    View view;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    myAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listening_list2, container, false);

        listView=view.findViewById(R.id.intermediate_listening_list);

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
            String AudioUrl = hashMap.get("url");
            String Title2 = hashMap.get("title2");

            title.setText(Title);



            linearLayout.setOnClickListener(view1 -> {

                ListeningFrag.QUESTION_URL=Link;
                ListeningFrag.TITLE=Title2;
                ListeningFrag.AUDIO_URL=AudioUrl;
                FragmentManager fragment = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay,new ListeningFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            });


            return myView;
        }
    }

    private void createTable() {
        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 1");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_1.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_1.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_1_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 2");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_2.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_2.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_2_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 3");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_3.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_3.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_3_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 4");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_4.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_4.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_4_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 5");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_5.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_5.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_5_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 6");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_6.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_6.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_6_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 7");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_7.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_7.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_7_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 8");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_8.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_8.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_8_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 9");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_9.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_9.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_9_adv.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Intermediate Listening 10");
        hashMap.put("title2", "Fill in the information");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_10.json");
        hashMap.put("url", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_10.mp3");
        hashMap.put("adv", "https://worldgalleryinc.com/apps/ielts_preparation/listening_questions/i_10_adv.json");
        arrayList.add(hashMap);

    }
}