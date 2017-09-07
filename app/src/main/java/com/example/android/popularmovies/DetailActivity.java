package com.example.android.popularmovies;

import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import static com.example.android.popularmovies.R.dimen.dimen_16;

public class DetailActivity extends AppCompatActivity {
    public static final String TAG = "DetailActivity";
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private Movie mMovie;
    private String[] mReviews;
    private ArrayAdapter<String> mReviewAdapter;
    private String[] mTrailerURLs;

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
        Log.d(TAG, "onCreate: " + mMovie.id);
        if(mMovie != null){
            Log.d(TAG, "onCreate: Intent started");

            // 1 - imageURL, 2 - title, 3 - release date, 4 - rating, 5 - synopsis
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener(){
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    picasso.load(R.drawable.filler).fit().into(mPoster);
                }
            });
            builder.build().load(mMovie.imageURL).fit().into(mPoster);
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

    class GetTrailersAndReviews extends AsyncTask<String, Void, String> {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... apiKey) {
            Log.d(TAG, "doInBackground: DetailActivity");
            String urlString = "https://api.themoviedb.org/3/movie/";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString + Long.toString(mMovie.id) + "/reviews?&api_key=" + apiKey[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod(REQUEST_METHOD);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                int response = urlConnection.getResponseCode();
//                Log.d(TAG, "doInBackground: The response code was " + response);

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
//
//                Log.d(TAG, "doInBackground: " + stringBuilder.toString());

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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                mReviews = MovieDataBaseUtils.getReviewsFromJSON(s);
                if (mReviews.length != 0) {
//                    Log.d(TAG, "onPostExecute: " + mReviews[0]);
                    LinearLayout linearlayout = (LinearLayout) findViewById(R.id.review_layout);
                    for (int i = 0; i < mReviews.length; i++){
                        TextView reviewText = new TextView(DetailActivity.this);
                        reviewText.setText(mReviews[i]);
                        reviewText.setTextSize(16);
                        int dimen_8 = getResources().getDimensionPixelSize(dimen_16);
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

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            mMovie.favourite = TRUE;
        } else {
            Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Long.toString(mMovie.id)).build();
            getContentResolver().delete(uri, null, null);
            Toast.makeText(getBaseContext(), "Deleted " + mMovie.name, Toast.LENGTH_SHORT).show();
            mMovie.favourite = FALSE;
        }
        
    }
}
