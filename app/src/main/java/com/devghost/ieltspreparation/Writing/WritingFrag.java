package com.devghost.ieltspreparation.Writing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devghost.ieltspreparation.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class WritingFrag extends Fragment {

    View view;
    ZoomageView imageView;
    TextView Title, ans;
    Button btn;
    String Ans;
    public static String WRITING_URL="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.writing_lay_design, container, false);

        imageView = view.findViewById(R.id.q_pic2);
        Title = view.findViewById(R.id.writing_title_tv);
        ans = view.findViewById(R.id.writing_answer_tv);
        btn = view.findViewById(R.id.writing_show_ans_btn);

        // get the JSON from server
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = WRITING_URL;

        // Request a JSON object response from the provided URL.
        // ...
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        // Access the JSON values from the response
                        String title = response.getString("title");
                         Ans = response.getString("ans");
                        String link = response.getString("link");

                        if(link.isEmpty() || title.isEmpty() || Ans.isEmpty()){
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Coming Soon")
                                    .setMessage("check after few days")
                                    .create()
                                    .show();

                        }
                        else {
                            Title.setText(title);


                            // Load image using Picasso
                            Picasso.get()
                                    .load(link)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(imageView);
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
            ans.setText(Ans);
            ans.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
        });

        return view;
    }
}
