package com.devghost.ieltspreparation.Speaking;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.MessageFormat;

public class SpeakingFrag extends Fragment {

    TextView tvTitle,tvDes,tvAns;
    Button btn;
    View view;
    public static String SPEAKING_URL ="";

    String Ans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view =  inflater.inflate(R.layout.fragment_speaking, container, false);

        //assignIds
        assignIds();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = SPEAKING_URL;

        // Request a JSON object response from the provided URL.
        // ...
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        // Access the JSON values from the response
                        String title = response.getString("title");
                        String des = response.getString("des");
                        Ans = response.getString("ans");


                        if(title.isEmpty() || Ans.isEmpty() || des.isEmpty()){
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Coming Soon")
                                    .setMessage("check after few days")
                                    .create()
                                    .show();

                        }
                        else {
                            tvTitle.setText(MessageFormat.format("Topic {0}", title));
                            tvDes.setText(des);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show());
// ...


        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


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
}