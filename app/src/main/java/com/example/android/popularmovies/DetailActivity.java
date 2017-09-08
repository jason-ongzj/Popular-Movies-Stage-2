package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utils.MovieDataBaseUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView mPoster = (ImageView) findViewById(R.id.Poster);
        TextView mRating = (TextView) findViewById(R.id.Rating);
        TextView mReleaseDate = (TextView) findViewById(R.id.Release_Date);
        TextView mSynopsis = (TextView) findViewById(R.id.Synopsis);
        TextView mTitle = (TextView) findViewById(R.id.Title);
        ToggleButton mToggle = (ToggleButton) findViewById(R.id.favorite);

        mMovie = getIntent().getExtras().getParcelable("Movie");
        if(mMovie != null){
            Log.d(TAG, "onCreate: Intent started");

            // 1 - imageURL, 2 - title, 3 - release date, 4 - rating, 5 - synopsis
            Picasso.with(this).load(mMovie.imageURL).fit().into(mPoster);
            mTitle.setText(mMovie.name);
            mReleaseDate.setText("Release Date: " + mMovie.date);

            if (mMovie.rating == 0) {
                mRating.setText("No Rating");
            } else mRating.setText("Rating: " + Double.toString(mMovie.rating) + "/10");

            if (mMovie.synopsis.matches("")){
                mSynopsis.setText("No synopsis given.");
            } else mSynopsis.setText(mMovie.synopsis);

            if(mMovie.favourite == Movie.TRUE) {
                mToggle.setChecked(true);
            } else {
                mToggle.setChecked(false);
            }

            new GetTrailersAndReviews().execute(BuildConfig.MOVIES_DB_API_KEY);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class GetTrailersAndReviews extends AsyncTask<String, Void, String[]> {

        private static final String REQUEST_METHOD = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String[] doInBackground(String... apiKey) {
            Log.d(TAG, "doInBackground: DetailActivity");
            String urlString = "https://api.themoviedb.org/3/movie/";
            HttpURLConnection urlConnection = null;
            HttpURLConnection trailer_urlConnection = null;
            try {
                URL url = new URL(urlString + Long.toString(mMovie.id) + "/reviews?&api_key=" + apiKey[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod(REQUEST_METHOD);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);

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

                URL url_trailers = new URL(urlString + Long.toString(mMovie.id) + "/videos?&api_key=" + apiKey[0]);
                trailer_urlConnection = (HttpURLConnection) url_trailers.openConnection();

                trailer_urlConnection.setRequestMethod(REQUEST_METHOD);
                trailer_urlConnection.setReadTimeout(READ_TIMEOUT);
                trailer_urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                int response_trailer = trailer_urlConnection.getResponseCode();
                Log.d(TAG, "doInBackground: The response code was " + response_trailer);

                trailer_urlConnection.connect();

                InputStreamReader streamReader_trailer = new InputStreamReader(trailer_urlConnection.getInputStream());
                BufferedReader bufferedReader_trailer = new BufferedReader(streamReader_trailer);
                StringBuilder stringBuilder_trailer = new StringBuilder();
                String line_trailer;
                while ((line_trailer = bufferedReader_trailer.readLine()) != null) {
                    stringBuilder_trailer.append(line_trailer).append("\n");
                }
                bufferedReader_trailer.close();
                streamReader_trailer.close();

                Log.d(TAG, "doInBackground: " + stringBuilder_trailer.toString());

                return new String[] {stringBuilder.toString(), stringBuilder_trailer.toString()};

            } catch(IOException e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(trailer_urlConnection != null){
                    trailer_urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            try {
                String [] mReviews = MovieDataBaseUtils.getReviewsFromJSON(s[0]);
//                String[] mVideos = MovieDataBaseUtils.getVideosFromJSON(s[1]);
                ArrayList<String> mVideos = MovieDataBaseUtils.getVideosFromJSON(s[1]);
                if (mReviews.length != 0) {
                    LinearLayout linearlayout = (LinearLayout) findViewById(R.id.review_layout);
                    for (int i = 0; i < mReviews.length; i++){
                        TextView reviewText = new TextView(DetailActivity.this);
                        reviewText.setText(mReviews[i]);
                        reviewText.setTextSize(16);
                        int dimen_8 = getResources().getDimensionPixelSize(R.dimen.dimen_8);
                        reviewText.setPadding(0,dimen_8,0,dimen_8);
                        reviewText.setTextColor(Color.WHITE);

                        View view = new View(DetailActivity.this);
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
                        view.setLayoutParams(layoutParams);
                        view.setBackgroundColor(Color.WHITE);
                        linearlayout.addView(reviewText);
                        if (i != mReviews.length - 1)
                            linearlayout.addView(view);
                    }
                }

                if(mVideos != null){
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.trailerLayout);
                    LayoutInflater layoutInflater = getLayoutInflater();
                    for (int i = 0; i < mVideos.size(); i++){
//                        if (mVideos[i] == "null") continue;
                        View child = layoutInflater.inflate(R.layout.trailers, null);
                        ImageView trailer_play_button = (ImageView) child.findViewById(R.id.videoPlayButton);
                        TextView trailer_textView = (TextView) child.findViewById(R.id.trailer);
                        String videoLink = "https://www.youtube.com/watch?v=" + mVideos.get(i);
                        final Uri videoUri = Uri.parse(videoLink);
                        int video_no = i + 1;
                        trailer_textView.setText("Video " + video_no);
                        trailer_play_button.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View v){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(videoUri);
                                Log.d(TAG, "onClick: " + videoUri.toString());
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    Log.d(TAG, "Couldn't call " + videoUri.toString());
                                }
                            }
                        });

                        linearLayout.addView(child);
                    }
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void onToggleClicked(View view){
        Log.d(TAG, "onToggleClicked: ");
        boolean on = ((ToggleButton) view).isChecked();

        if (on){
            ContentValues contentValues = new ContentValues();

            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.id);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE, mMovie.name);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_URL, mMovie.imageURL);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, mMovie.synopsis);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.date);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mMovie.rating);

            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            mMovie.favourite = TRUE;
        } else {
            Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Long.toString(mMovie.id)).build();
            getContentResolver().delete(uri, null, null);
            Toast.makeText(getBaseContext(), "Deleted " + mMovie.name, Toast.LENGTH_SHORT).show();
            mMovie.favourite = FALSE;
        }

    }
}
