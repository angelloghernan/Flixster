package com.example.flixster.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Review {
    String authorUsername;
    String avatarPath;
    Double rating;
    String content;
    String creationDate;

    final String AVATAR_URL = "https://www.themoviedb.org/t/p/w64_and_h64_face/";

    public Review() {

    }

    public Review(JSONObject results) throws JSONException {
        JSONObject author = results.getJSONObject("author_details");
        this.authorUsername = author.getString("username");
        this.avatarPath = author.getString("avatar_path").substring(1);
        if (!avatarPath.contains("gravatar")) {
            avatarPath = AVATAR_URL + avatarPath;
        }
        try{
            this.rating = author.getDouble("rating");
        } catch (JSONException e) {
            Log.w("review", "invalid rating parsed: " + e);
            this.rating = 0.0;
        }

        this.content = results.getString("content");
        this.creationDate = results.getString("created_at").split("T")[0];
    }

    public static List<Review> fromJSONArray(JSONArray reviewJSONArray) throws JSONException {
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < reviewJSONArray.length(); i++) {
            reviews.add(new Review(reviewJSONArray.getJSONObject(i)));
        }
        return reviews;
    }

    public String getAvatarPath(){
        return avatarPath;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public Double getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public String getCreationDate() {
        return creationDate;
    }
}
