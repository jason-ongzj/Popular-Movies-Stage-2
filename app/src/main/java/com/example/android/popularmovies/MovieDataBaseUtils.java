package com.example.android.popularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ben on 8/27/2017.
 */

public class MovieDataBaseUtils {

    public static String[] getImageFromJson(Context context, String response)
        throws JSONException{
        String[] parsedMovieImageURL = null;
        JSONObject movieJSON = new JSONObject(response);
        JSONArray movieResultsJSON = movieJSON.getJSONArray("results");
        parsedMovieImageURL = new String[movieResultsJSON.length()];
        for (int i = 0; i < movieResultsJSON.length(); i++){
            JSONObject movie = movieResultsJSON.getJSONObject(i);
            String image_url = "https://image.tmdb.org/t/p/w185" + movie.getString("poster_path");
            parsedMovieImageURL[i] = image_url;
        }
        return parsedMovieImageURL;
    }
}
