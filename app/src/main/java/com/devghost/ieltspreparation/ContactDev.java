package com.devghost.ieltspreparation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class ContactDev extends Fragment {

    View view;
    LinearLayout contactDev;
    LinearLayout RateUs,logOut;
    TextView pp;

    ListView listView;
    ListAdapter listAdapter;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;

    int ALREADY_RATED = 0;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_dev, container, false);

        contactDev=view.findViewById(R.id.contactDevLay);
        RateUs=view.findViewById(R.id.rateUs);
        logOut=view.findViewById(R.id.logout_id);
        pp=view.findViewById(R.id.pp);
        listView=view.findViewById(R.id.recent_list);

        loadData();
        if(ALREADY_RATED>0){
            RateUs.setVisibility(View.GONE);
        }

        if(firebaseAuth.getCurrentUser()==null){
            logOut.setVisibility(View.GONE);
        }

        contactDev.setOnClickListener(view -> {
            Toast.makeText(requireContext(), "Email Us , Select Your Email/Gmail App", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"jyspark668@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Email From Ielts App");
            i.putExtra(Intent.EXTRA_TEXT   , "Write here");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });
        RateUs.setOnClickListener(v -> {
            ALREADY_RATED+=1;
            saveData();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + requireContext().getPackageName())));
            }
            catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/apps/details?id=" + requireContext().getPackageName())));
            }
        });
        logOut.setOnClickListener(v -> logOut());
        pp.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(requireActivity(), Uri.parse(getString(R.string.pp_link)));
        });


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

    private void saveData(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("info",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("data",ALREADY_RATED);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("info",MODE_PRIVATE);
        int data = sharedPreferences.getInt("data",0);
       ALREADY_RATED+=data;
    }

    private void logOut(){
        AlertDialog.Builder alertdialogbuilder;
        alertdialogbuilder = new AlertDialog.Builder(requireContext());
        // alertdialogbuilder.setIcon(R.drawable.a);
        alertdialogbuilder.setTitle("Sign Out ?");
        alertdialogbuilder.setMessage("Do you want to log out ?");
        alertdialogbuilder.setCancelable(false);

        alertdialogbuilder.setPositiveButton("yes", (dialogInterface, i) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(requireContext(),LoginOrSignUp.class));
        });

        alertdialogbuilder.setNegativeButton("no", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialog = alertdialogbuilder.create();
        alertDialog.show();
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