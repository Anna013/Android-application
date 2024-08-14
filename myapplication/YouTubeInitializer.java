package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YouTubeInitializer {

    private static YouTubePlayer.OnInitializedListener lis;
    private static String movie_key;


    public static void getMovieYouTubeKey(JSONObject response, Context con, YouTubePlayerView player ) throws JSONException {

        JSONArray jsonArray = response.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject resoult = jsonArray.getJSONObject(i);
            String name = resoult.getString("name");

            if (name.contains("Trailer")) {
                movie_key = resoult.getString("key");
                if (name.contains("Official")) {
                    movie_key = resoult.getString("key");
                    break;
                }
            }
        }

        initializeYoutube(con, player);
    }

    public static void initializeYoutube(Context con, YouTubePlayerView player) {

        lis = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(movie_key);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(con, "YouTube error!", Toast.LENGTH_LONG)
                        .show();
            }
        };

        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.initialize("apiKey", lis);
            }
        });
    }
}
