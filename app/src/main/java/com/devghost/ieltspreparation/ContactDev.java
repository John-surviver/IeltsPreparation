package com.devghost.ieltspreparation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class ContactDev extends Fragment {

    View view;

    ListView listView;
    ListAdapter listAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_dev, container, false);


        listView=view.findViewById(R.id.recent_list);



        // get the json from server
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://codemind.live/apps/ielts/updates/udates.json";

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {

            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String title = jsonObject.getString("title");
                    String date = jsonObject.getString("date");



                    hashMap = new HashMap<>();
                    hashMap.put("title",title);
                    hashMap.put("date",date);
                    arrayList.add(hashMap);
                }
                listAdapter = new ListAdapter();
                listView.setAdapter(listAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);


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
            @SuppressLint("ViewHolder") View myView = layoutInflater.inflate(R.layout.recent_list_design,viewGroup,false);


            TextView number = myView.findViewById(R.id.num);
            TextView title = myView.findViewById(R.id.recent_txt);
            TextView date = myView.findViewById(R.id.date_tv);



            HashMap<String,String> hashMap= arrayList.get(position) ;
            String Title = hashMap.get("title");
            String Date = hashMap.get("date");

            title.setText(Title);
            date.setText(Date);

            //  Words.setText(Title);
            number.setText(MessageFormat.format("{0}", position+1));



            return myView;
        }
    }
}