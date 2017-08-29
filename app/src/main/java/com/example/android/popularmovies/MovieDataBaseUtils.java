package com.example.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ben on 8/27/2017.
 */

public class MovieDataBaseUtils {

    public static JSONArray getResults(String response){
        try {
            JSONObject movieJSON = new JSONObject(response);
            JSONArray movieResultsJSON = movieJSON.getJSONArray("results");
            return movieResultsJSON;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getImageFromJson(String response) throws JSONException{
        JSONArray movieResultsJSON = getResults(response);
        if (movieResultsJSON != null) {
            String[] parsedMovieImageURL = new String[movieResultsJSON.length()];
            for (int i = 0; i < movieResultsJSON.length(); i++) {
                JSONObject movie = movieResultsJSON.getJSONObject(i);
                String image_url = "https://image.tmdb.org/t/p/w185" + movie.getString("poster_path");
                parsedMovieImageURL[i] = image_url;
            }
            return parsedMovieImageURL;
        }
        return null;
    }

    public static String[] getMovieTitles(String response) throws JSONException {
        JSONArray movieResultsJSON = getResults(response);
        if (movieResultsJSON != null) {
            String[] movieTitle = new String[movieResultsJSON.length()];
            for (int i = 0; i < movieResultsJSON.length(); i++) {
                JSONObject movie = movieResultsJSON.getJSONObject(i);
                movieTitle[i] = movie.getString("title");
            }
            return movieTitle;
        }
        return null;
    }

    public static String[] getVoteAverage(String response) throws JSONException {
        JSONArray movieResultsJSON = getResults(response);
        if (movieResultsJSON != null) {
            String[] voteAverage = new String[movieResultsJSON.length()];
            for (int i = 0; i < movieResultsJSON.length(); i++) {
                JSONObject movie = movieResultsJSON.getJSONObject(i);
                voteAverage[i] = movie.getString("vote_average");
            }
            return voteAverage;
        }
        return null;
    }

    public static String[] getSynopses(String response) throws JSONException {
        JSONArray movieResultsJSON = getResults(response);
        if (movieResultsJSON != null) {
            String[] movieSynopses = new String[movieResultsJSON.length()];
            for (int i = 0; i < movieResultsJSON.length(); i++) {
                JSONObject movie = movieResultsJSON.getJSONObject(i);
                movieSynopses[i] = movie.getString("overview");
            }
            return movieSynopses;
        }
        return null;
    }

    public static String[] getReleaseDate(String response) throws JSONException {
        JSONArray movieResultsJSON = getResults(response);
        if (movieResultsJSON != null) {
            String[] releaseDate = new String[movieResultsJSON.length()];
            for (int i = 0; i < movieResultsJSON.length(); i++) {
                JSONObject movie = movieResultsJSON.getJSONObject(i);
                releaseDate[i] = movie.getString("release_date");
            }
            return releaseDate;
        }
        return null;
    }
}
