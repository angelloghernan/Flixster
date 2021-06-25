package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.ReviewAdapter;
import com.example.flixster.models.Movie;
import com.example.flixster.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {
    String TAG = "MovieDetailsActivity";

    Movie movie;

    TextView tvDetailsTitle;
    TextView tvDetailsOverview;
    RatingBar rbVoteAverage;
    ImageView ivDetailsPoster;

    List<Review> reviews;


    String videoID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        RecyclerView rvReviews = findViewById(R.id.rvReviews);
        reviews = new ArrayList<>();

        // create review adapter
        final ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviews);

        // set adapter on recycler view
        rvReviews.setAdapter(reviewAdapter);

        // set layout manager on rv
        rvReviews.setLayoutManager(new LinearLayoutManager(this));



        // unwrap movie passed by the intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        // get view objects
        tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        tvDetailsOverview = (TextView) findViewById(R.id.tvDetailsOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        ivDetailsPoster = (ImageView) findViewById(R.id.ivDetailsPoster);


        tvDetailsTitle.setText(movie.getTitle());
        tvDetailsOverview.setText(movie.getOverview());

        Glide.with(this)
                .load(movie.getBackdropPath())
                .placeholder(R.drawable.placeholder)
                .into(ivDetailsPoster);


        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        AsyncHttpClient client = new AsyncHttpClient();

        String VIDEO_REQUEST_URL = "https://api.themoviedb.org/3/movie/"
                + movie.getId().toString() + "/videos?api_key=" + "abfdb5b3b4fafbbec360f10a7c9d08b0"
                + "&language=en-US";

        client.get(VIDEO_REQUEST_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    videoID = results.getJSONObject(1).getString("key");
                    Log.i(TAG, "found video at id " + videoID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "error fetching json from moviedb video request");
            }
        });

        client.get("https://api.themoviedb.org/3/movie/" + movie.getId().toString()
                + "/reviews?api_key=" + "abfdb5b3b4fafbbec360f10a7c9d08b0", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    reviews.addAll(Review.fromJSONArray(results));
                    reviewAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Results: " + results.toString());

                } catch (JSONException e) {
                    Log.d(TAG, "error fetching json from moviedb reviews request");
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public void onClick(View v)
    {
        if (videoID != null){
            Intent intent = new Intent(this, MovieTrailerActivity.class);
            intent.putExtra("videoID", videoID);
            this.startActivity(intent);
        }
    }
}