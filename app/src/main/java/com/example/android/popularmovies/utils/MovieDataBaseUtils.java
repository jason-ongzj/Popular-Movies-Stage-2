package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDataBaseUtils {

    private static final int FALSE = 0;

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

                Long id = movieJSON.getLong("id");

                String synopsis = movieJSON.getString("overview");

                String posterPath = movieJSON.getString("poster_path");
                String imageUrl = "https://image.tmdb.org/t/p/w185" + posterPath;

                String date = movieJSON.getString("release_date");

                Double voteAverage = movieJSON.getDouble("vote_average");

                Movie movie = new Movie(title, id, synopsis, imageUrl, date, voteAverage, Movie.FALSE);
                movieArrayList.add(i, movie);

            }
            return movieArrayList;
        }
        return null;
    }

    public static String[] getReviewsFromJSON(String response) throws JSONException {
        JSONArray movieResultsJSON = getResults(response);
        if (movieResultsJSON != null) {
            String[] reviewList = new String[movieResultsJSON.length()];
            for (int i = 0; i < movieResultsJSON.length(); i++) {
                JSONObject movieJSON = movieResultsJSON.getJSONObject(i);
                String review = movieJSON.getString("content");
                reviewList[i] = review;
            }
            return reviewList;
        }
        return null;
    }
}
