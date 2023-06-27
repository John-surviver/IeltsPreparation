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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.R;
import com.devghost.ieltspreparation.Reading.ReadingFrag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WritingList2 extends Fragment {

    View view;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    myAdapter listAdapter;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.listening_list, container, false);

        listView=view.findViewById(R.id.basic_listening_list);
        progressBar=view.findViewById(R.id.loading_list);

        if (listAdapter == null) {
            listAdapter = new myAdapter();
        }
        listView.setAdapter(listAdapter);

        arrayList.clear();

        // get the json from server
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://codemind.live/apps/ielts/writing/writing2/get.php";

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            progressBar.setVisibility(View.GONE);
            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String words = jsonObject.getString("name");
                    String id = jsonObject.getString("id");


                    hashMap = new HashMap<>();
                    hashMap.put("title",words);
                    hashMap.put("id",id);
                    arrayList.add(hashMap);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            //  progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

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
            String titleValue = hashMap.get("title");
            String idValue = hashMap.get("id");

            title.setText(titleValue);

            linearLayout.setOnClickListener(view1 -> {
                assert idValue != null;
                if (!idValue.isEmpty()) {
                    try {
                        int number = Integer.parseInt(idValue);
                        WritingFrag.ID = number;
                        WritingFrag.WRITING_URL="https://codemind.live/apps/ielts/writing/writing2/get.php";
                        FragmentManager fragment = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragment.beginTransaction();
                        fragmentTransaction.replace(R.id.mainLay, new WritingFrag());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // Handle invalid number format
                    }
                }
            });

            return myView;
        }
    }




}