package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements ImageDisplayAdapter.ImageDisplayAdapterOnClickHandler{
    private static final String TAG = "MainActivity";
    private TextView mErrorDisplay;
    private ImageDisplayAdapter mImageDisplayAdapter;
    private RecyclerView mRecyclerView;
    private static int DISPLAY_STATE = 0;

    // TODO: Fill in your own API key here
    private static final String api_key = "";

    private final static String BUNDLE_RECYCLER_LAYOUT = "Recycler_Layout";
    private Parcelable savedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorDisplay = (TextView) findViewById(R.id.error_display);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mImageDisplayAdapter = new ImageDisplayAdapter(this);
        mImageDisplayAdapter.setContext(this);

        mRecyclerView.setAdapter(mImageDisplayAdapter);

        mRecyclerView.setHasFixedSize(true);

        displayOnRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.most_popular:
                DISPLAY_STATE = 0;
                Log.d(TAG, "onOptionsItemSelected: State " + DISPLAY_STATE);
                displayOnRequest();
                return true;
            case R.id.top_rated:
                DISPLAY_STATE = 1;
                Log.d(TAG, "onOptionsItemSelected: State " + DISPLAY_STATE);
                displayOnRequest();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savedRecyclerLayoutState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, savedRecyclerLayoutState);
        Log.d(TAG, "onSaveInstanceState: Saving recycler view");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null){
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        Log.d(TAG, "onRestoreInstanceState: Restoring recycler view");
    }

    // Pass data through a single string array into intent
    @Override
    public void onDisplayImageClicked(String[] movieData) {
        Class destinationActivity = DetailActivity.class;
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra("movieData", movieData);
        startActivity(intent);
    }

    private boolean displayOnRequest(){
        if(!isInternetConnected()){
            showDisplayError();
            return false;
        } else {
            new RetrieveFeedTask().execute(api_key);
            showMovieCatalogue();
            return true;
        }
    }

    private boolean isInternetConnected(){
        Context context = MainActivity.this;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void showDisplayError(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorDisplay.setVisibility(View.VISIBLE);
    }

    private void showMovieCatalogue(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorDisplay.setVisibility(View.INVISIBLE);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "AsyncTask";
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... apiKey) {
            Log.d(TAG, "doInBackground: ");

            String urlString = "";
            HttpURLConnection urlConnection = null;
            if (DISPLAY_STATE == 0) {
                urlString = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc?";
            } else if (DISPLAY_STATE == 1){
                urlString = "https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc";
            }
            try {
                URL url = new URL(urlString + "&api_key=" + apiKey[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                //TODO: Add test for internet connection later

                urlConnection.setRequestMethod(REQUEST_METHOD);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                int response = urlConnection.getResponseCode();
                Log.d(TAG, "doInBackground: The response code was " + response);

                urlConnection.connect();

                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                streamReader.close();

                Log.d(TAG, "doInBackground: " + MovieDataBaseUtils.getResults(stringBuilder.toString()));

                return stringBuilder.toString();

            } catch(IOException e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(String movieResults) {
            try {
                if (movieResults != null) {
                // Set data to be retrieved when DetailActivity is called
                    mImageDisplayAdapter.setImageData(MovieDataBaseUtils.getImageFromJson(movieResults));

                    mImageDisplayAdapter.setTitle(MovieDataBaseUtils.getMovieTitles(movieResults));

                    mImageDisplayAdapter.setReleaseDates(MovieDataBaseUtils.getReleaseDate(movieResults));

                    mImageDisplayAdapter.setSynopsis(MovieDataBaseUtils.getSynopses(movieResults));

                    mImageDisplayAdapter.setVoteAverage(MovieDataBaseUtils.getVoteAverage(movieResults));

                    showMovieCatalogue();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
