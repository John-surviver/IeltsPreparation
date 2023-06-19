package com.devghost.ieltspreparation.Writing;

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
import com.devghost.ieltspreparation.Reading.ReadingFrag;

import java.util.ArrayList;
import java.util.HashMap;

public class WritingList3 extends Fragment {

    View view;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    myAdapter listAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_writing_list1, container, false);

        listView=view.findViewById(R.id.basic_writing_list);

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
            String Title2 = hashMap.get("title2");

            title.setText(Title);



            linearLayout.setOnClickListener(view1 -> {

                WritingFrag.WRITING_URL=Link;
                ReadingFrag.TITLE=Title2;
                FragmentManager fragment = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragment.beginTransaction();
                fragmentTransaction.replace(R.id.mainLay,new WritingFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            });


            return myView;
        }
    }

    private void createTable() {
        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 1");
        hashMap.put("title2", "Passage: Antarctica");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_1.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 2");
        hashMap.put("title2", "Library Card");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_2.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 3");
        hashMap.put("title2", "Some Title 3");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_3.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 4");
        hashMap.put("title2", "Some Title 4");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_4.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 5");
        hashMap.put("title2", "Some Title 5");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_5.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 6");
        hashMap.put("title2", "Some Title 6");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_6.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 7");
        hashMap.put("title2", "Some Title 7");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_7.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 8");
        hashMap.put("title2", "Some Title 8");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_8.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 9");
        hashMap.put("title2", "Some Title 9");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_9.json");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Advanced 10");
        hashMap.put("title2", "Some Title 10");
        hashMap.put("link", "https://worldgalleryinc.com/apps/ielts_preparation/writing_questions/a_10.json");
        arrayList.add(hashMap);
    }

}