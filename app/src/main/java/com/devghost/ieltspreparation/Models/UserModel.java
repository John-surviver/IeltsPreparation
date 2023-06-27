package com.devghost.ieltspreparation.Models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private final String name;
    private final int points;
    private String profilePicUrl;

    public UserModel(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public static String toJsonArray(List<UserModel> userModelList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (UserModel userModel : userModelList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", userModel.getName());
                jsonObject.put("points", userModel.getPoints());
                jsonObject.put("profilePicUrl", userModel.getProfilePicUrl()); // Include profilePicUrl in the JSON
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public static List<UserModel> fromJsonArray(String jsonArrayString) {
        List<UserModel> userModelList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                int points = jsonObject.optInt("points");
                String profilePicUrl = jsonObject.optString("profilePicUrl"); // Retrieve profilePicUrl from JSON
                UserModel userModel = new UserModel(name, points);
                userModel.setProfilePicUrl(profilePicUrl); // Set profilePicUrl
                userModelList.add(userModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userModelList;
    }
}

