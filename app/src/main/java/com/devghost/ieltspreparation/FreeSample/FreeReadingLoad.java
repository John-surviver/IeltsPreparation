package com.devghost.ieltspreparation.FreeSample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FreeReadingLoad extends Fragment {

    TextView tvTitle,tvDes;
    Button btn;
    View view;
    public static String SPEAKING_URL ="";
    public static int ID = 0;

    HashMap<String, String> hashMap;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_free_reading_load, container, false);

        //assignIds
        assignIds();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = SPEAKING_URL;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
//            progressBar.setVisibility(View.GONE);
            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String name = jsonObject.getString("name");
                    String passage = jsonObject.getString("passage");


                    hashMap = new HashMap<>();
                    hashMap.put("name",name);
                    hashMap.put("passage",passage);
                    arrayList.add(hashMap);

                }

                int id = ID-1;
                HashMap<String, String> hashMap = arrayList.get(id);
                String Name = hashMap.get("name");
                String Passage = hashMap.get("passage");

                tvTitle.setText(Name);
                tvDes.setText(Passage);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // progressBar.setVisibility(View.GONE);
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);



        return view;
    }

    private void assignIds() {
        tvTitle=view.findViewById(R.id.free_reading_title);
        tvDes=view.findViewById(R.id.free_reading_des);
    }
}