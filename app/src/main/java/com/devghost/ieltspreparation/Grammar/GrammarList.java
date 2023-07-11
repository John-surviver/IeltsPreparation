package com.devghost.ieltspreparation.Grammar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.devghost.ieltspreparation.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class GrammarList extends Fragment {

    View view;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    myAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grammar_list, container, false);

        listView=view.findViewById(R.id.grammar_list);

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
            TextView number = myView.findViewById(R.id.item_num);
            LinearLayout linearLayout = myView.findViewById(R.id.list_lay);

            HashMap<String, String> hashMap = arrayList.get(position);
            String Title = hashMap.get("title");
            String Link = hashMap.get("link");

            title.setText(Title);
            number.setText(MessageFormat.format("{0}", position + 1));


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
        hashMap.put("title", "Noun");
        hashMap.put("link","file:///android_asset/noun.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Pronoun");
        hashMap.put("link","file:///android_asset/pronoun.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Verb");
        hashMap.put("link","file:///android_asset/verb.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Adverb");
        hashMap.put("link","file:///android_asset/adverb.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Adjectives");
        hashMap.put("link","file:///android_asset/adjective.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Conjunction");
        hashMap.put("link","file:///android_asset/conjunction.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Interjections");
        hashMap.put("link","file:///android_asset/interjection.html");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Prepositions");
        hashMap.put("link","file:///android_asset/prepositions.html");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("title", "Present Tense");
        hashMap.put("link","file:///android_asset/present.html");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("title", "Past Tense");
        hashMap.put("link","file:///android_asset/past.html");
        arrayList.add(hashMap);


        hashMap = new HashMap<>();
        hashMap.put("title", "Future Tense");
        hashMap.put("link","file:///android_asset/future.html");
        arrayList.add(hashMap);


    }
}