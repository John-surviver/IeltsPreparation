package com.devghost.ieltspreparation.Writing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.Models.DatabaseHelper;
import com.devghost.ieltspreparation.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WritingFrag extends Fragment {

    View view;
    ZoomageView imageView;
    TextView Title, ans;
    Button btn;
    String Ans;
    public static int ID=0;
    public static String WRITING_URL="";
    HashMap<String, String> hashMap;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    private DatabaseHelper databaseHelper;

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.writing_lay_design, container, false);

        imageView = view.findViewById(R.id.q_pic2);
        Title = view.findViewById(R.id.writing_title_tv);
        ans = view.findViewById(R.id.writing_answer_tv);
        btn = view.findViewById(R.id.writing_show_ans_btn);
        databaseHelper = new DatabaseHelper(requireContext());

        // Save the scores to the database
        int[] Scores = databaseHelper.getScores();

        Scores[3]+=Scores[3]+2;

        saveScores(Scores);

        // get the JSON from server
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = WRITING_URL;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
//            progressBar.setVisibility(View.GONE);
            try {

                for(int x= 0 ; x<response.length(); x++){
                    JSONObject jsonObject = response.getJSONObject(x);
                    String title = jsonObject.getString("title");
                    String pic = jsonObject.getString("pic");
                    String ans = jsonObject.getString("ans");


                    hashMap = new HashMap<>();
                    hashMap.put("title",title);
                    hashMap.put("pic",pic);
                    hashMap.put("ans",ans);
                    arrayList.add(hashMap);


                 //   String array = String.valueOf(arrayList.get(Integer.parseInt(ID)));

                }

                int id = ID-1;
                HashMap<String, String> hashMap = arrayList.get(id);
                String Titles = hashMap.get("title");
                String Pic = hashMap.get("pic");
                Ans = hashMap.get("ans");

                Title.setText(Titles);


                // Load image using Picasso
                Picasso.get()
                        .load(Pic)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
           // progressBar.setVisibility(View.GONE);
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);



        btn.setOnClickListener(v -> {
            ans.setText(Ans);
            ans.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
        });

        return view;
    }

    private void saveScores(int[] scores) {
        databaseHelper.saveScores(scores);
    }
}
