package com.devghost.ieltspreparation;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devghost.ieltspreparation.Models.UserModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RankingFrag extends Fragment {

    private static final String PREF_NAME = "RankingPrefs";
    private static final String KEY_RANKING_DATA = "rankingData";
    private static final String BUCKET_NAME = "studentsjob-b131d.appspot.com"; // Replace with your actual bucket name

    private ListView rankingListView;
    private RankingAdapter rankingAdapter;
    private List<UserModel> rankingList;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingListView = view.findViewById(R.id.ranking_list_view);
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        fetchDataFromFirestore();

        return view;
    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .orderBy("points", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        rankingList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            String name = document.getString("UserName");
                            String poin = document.getString("points");
                            int points = Integer.parseInt(Objects.requireNonNull(poin));
                            String userId = document.getId();

                            // Construct the profile picture URL
                            String profilePicUrl = "gs://" + BUCKET_NAME + "/profile_images/" + document.getId() + ".jpg";
                            getProfilePicUrl(userId, profilePicUrl); // Retrieve the profile picture URL

                            UserModel userModel = new UserModel(name, points);
                            rankingList.add(userModel);
                        }

                        // Sort the ranking list based on points in descending order
                        rankingList.sort((item1, item2) -> Integer.compare(item2.getPoints(), item1.getPoints()));

                        saveRankingData(rankingList); // Save the ranking data to SharedPreferences

                    } else {
                        Toast.makeText(getActivity(), "Failed to fetch ranking", Toast.LENGTH_SHORT).show();
                        rankingList = getRankingData(); // Load the ranking data from SharedPreferences
                        new AlertDialog.Builder(requireContext())
                                .setTitle("")
                                .setMessage("Failed to fetch ranking")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    rankingList = getRankingData(); // Load the ranking data from SharedPreferences
                                    rankingAdapter = new RankingAdapter(rankingList);
                                    rankingListView.setAdapter(rankingAdapter);
                                })
                                .create()
                                .show();
                    }
                    rankingAdapter = new RankingAdapter(rankingList);
                    rankingListView.setAdapter(rankingAdapter);
                });
    }

    private void getProfilePicUrl(String userId, String profilePicUrl) {
        // Construct the Firebase Storage reference to the user's profile picture
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(BUCKET_NAME);
        StorageReference profilePicRef = storageRef.child("profile_images/" + userId + ".jpg");

        // Get the download URL for the profile picture
        profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();

            // Update the profile picture URL for the corresponding user in the ranking list
            for (UserModel userModel : rankingList) {
                if (userModel.getProfilePicUrl().equals(profilePicUrl)) {
                    userModel.setProfilePicUrl(downloadUrl);
                    break;
                }
            }

            rankingAdapter.notifyDataSetChanged();
        }).addOnFailureListener(exception -> {
            // Handle any errors that occurred while retrieving the profile picture URL
        });
    }


    private void saveRankingData(List<UserModel> rankingList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (UserModel userModel : rankingList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", userModel.getName());
                jsonObject.put("points", userModel.getPoints());
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String rankingDataJson = jsonArray.toString();
        sharedPreferences.edit().putString(KEY_RANKING_DATA, rankingDataJson).apply();
    }

    private List<UserModel> getRankingData() {
        String rankingDataJson = sharedPreferences.getString(KEY_RANKING_DATA, null);
        List<UserModel> userModelList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(rankingDataJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                int points = jsonObject.optInt("points");
                UserModel userModel = new UserModel(name, points);
                userModelList.add(userModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userModelList;
    }

    public class RankingAdapter extends BaseAdapter {
        private final List<UserModel> rankingList;

        public RankingAdapter(List<UserModel> rankingList) {
            this.rankingList = rankingList;
        }

        @Override
        public int getCount() {
            return rankingList.size();
        }

        @Override
        public Object getItem(int position) {
            return rankingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.ranking_lay_design, parent, false);
            }
            TextView textViewName = convertView.findViewById(R.id.rank_user_name_tv);
            TextView textViewPoints = convertView.findViewById(R.id.rank_user_points_tv);
            // ImageView userPic = convertView.findViewById(R.id.user_pic);

            String rankNum = MessageFormat.format("{0}", position + 1);

            UserModel item = rankingList.get(position);
            textViewName.setText(MessageFormat.format("{0}. {1}", rankNum, item.getName()));
            textViewPoints.setText(String.valueOf(item.getPoints()));



            // Load the profile picture using a library like Picasso, Glide, or any other image loading library
            // For example, using Picasso:
            // Picasso.get().load(item.getProfilePicUrl()).into(userPic);

            return convertView;
        }
    }
}
