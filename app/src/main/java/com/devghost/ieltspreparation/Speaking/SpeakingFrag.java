package com.devghost.ieltspreparation.Speaking;

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
import com.devghost.ieltspreparation.Models.DatabaseHelper;
import com.devghost.ieltspreparation.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SpeakingFrag extends Fragment {

    TextView tvTitle,tvDes,tvAns;
    Button btn;
    View view;
    public static String SPEAKING_URL ="";
    public static int ID = 0;

    String Ans;

    HashMap<String, String> hashMap;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view =  inflater.inflate(R.layout.fragment_speaking, container, false);

        //assignIds
        assignIds();
        databaseHelper = new DatabaseHelper(requireContext());

        // Save the scores to the database
        int[] Scores = databaseHelper.getScores();

        Scores[2]+=Scores[2]+2;

        saveScores(Scores);

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = SPEAKING_URL;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
//            progressBar.setVisibility(View.GONE);
            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String title = jsonObject.getString("title");
                    String name = jsonObject.getString("name");
                    String ans = jsonObject.getString("ans");


                    hashMap = new HashMap<>();
                    hashMap.put("title",title);
                    hashMap.put("name",name);
                    hashMap.put("ans",ans);
                    arrayList.add(hashMap);


                    //   String array = String.valueOf(arrayList.get(Integer.parseInt(ID)));

                }

                int id = ID-1;
                HashMap<String, String> hashMap = arrayList.get(id);
                String Titles = hashMap.get("title");
                String Name = hashMap.get("name");
                Ans = hashMap.get("ans");

                tvTitle.setText(Name);
                tvDes.setText(Titles);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // progressBar.setVisibility(View.GONE);
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);



        btn.setOnClickListener(v -> {
            tvAns.setText(Ans);
            tvAns.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
        });


       return view;
    }
    private void assignIds() {
        tvTitle=view.findViewById(R.id.speaking_title_tv);
        tvDes=view.findViewById(R.id.speaking_des_tv);
        tvAns=view.findViewById(R.id.speaking_ans_tv);
        btn=view.findViewById(R.id.speaking_ans_btn);
    }

    private void saveScores(int[] scores) {
        databaseHelper.saveScores(scores);
    }
}