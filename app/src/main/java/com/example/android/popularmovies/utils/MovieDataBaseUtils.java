package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDataBaseUtils {

    public static JSONArray getResults(String response){
        try {
            JSONObject movieJSON = new JSONObject(response);
            return movieJSON.getJSONArray("results");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Movie> getMovieObjectsFromJSON(String response) throws JSONException {
        JSONArray movieResultsJSON = getResults(response);
        if (movieResultsJSON != null){
            ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
            for (int i = 0; i < movieResultsJSON.length(); i++) {
                JSONObject movieJSON = movieResultsJSON.getJSONObject(i);

                String title = movieJSON.getString("title");

                int id = movieJSON.getInt("id");

                String synopsis = movieJSON.getString("overview");

                String posterPath = movieJSON.getString("poster_path");
                String imageUrl = "https://image.tmdb.org/t/p/w185" + posterPath;

                String date = movieJSON.getString("release_date");

                Double voteAverage = movieJSON.getDouble("vote_average");

                Movie movie = new Movie(title, id, synopsis, imageUrl, date, voteAverage);
                movieArrayList.add(i, movie);

            }
            return movieArrayList;
        }
        return null;
    }
}
