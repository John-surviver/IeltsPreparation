package com.devghost.ieltspreparation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class IdomsAndPhrases extends Fragment {

    View view;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    myAdapter listAdapter;
    ProgressBar progressBar;
    TextToSpeech textToSpeech;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.listening_list, container, false);

        listView=view.findViewById(R.id.basic_listening_list);
        progressBar=view.findViewById(R.id.loading_list);

        textToSpeech = new TextToSpeech(requireContext(), i -> {

        });

        if (listAdapter == null) {
            listAdapter = new myAdapter();
        }
        listView.setAdapter(listAdapter);
        arrayList.clear();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://codemind.live/apps/ielts/idioms/idioms.json";

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            progressBar.setVisibility(View.GONE);
            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String title = jsonObject.getString("part1");
                    String des = jsonObject.getString("part2");


                    hashMap = new HashMap<>();
                    hashMap.put("title",title);
                    hashMap.put("des",des);

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


        return  view;
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
            String Des = hashMap.get("des");

            title.setText(Title);



            linearLayout.setOnClickListener(view1 -> {

                textToSpeech.speak(Des,TextToSpeech.QUEUE_FLUSH,null,null);

                new AlertDialog.Builder(requireContext())
                        .setTitle("")
                        .setMessage(Des)
                        .create()
                        .show();

            });


            return myView;
        }
    }
}