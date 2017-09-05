package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    public static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView mPoster = (ImageView) findViewById(R.id.Poster);
        TextView mRating = (TextView) findViewById(R.id.Rating);
        TextView mReleaseDate = (TextView) findViewById(R.id.Release_Date);
        TextView mSynopsis = (TextView) findViewById(R.id.Synopsis);
        TextView mTitle = (TextView) findViewById(R.id.Title);

        Movie movie = getIntent().getExtras().getParcelable("Movie");
        if(movie != null){
            Log.d(TAG, "onCreate: Intent started");

            // 1 - imageURL, 2 - title, 3 - release date, 4 - rating, 5 - synopsis
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener(){
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    picasso.load(R.drawable.filler).fit().into(mPoster);
                }
            });
            builder.build().load(movie.imageURL).fit().into(mPoster);
            mTitle.setText(movie.name);
            mReleaseDate.setText("Release Date: " + movie.date);

            if (movie.rating == 0) {
                mRating.setText("No Rating");
            } else mRating.setText("Rating: " + Double.toString(movie.rating) + "/10");

            if (movie.synopsis.matches("")){
                mSynopsis.setText("No synopsis given.");
            } else mSynopsis.setText(movie.synopsis);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    class GetTrailersAndReviews extends AsyncTask<String[], Void, String>{

        @Override
        protected String doInBackground(String[]... urlStrings) {
            return null;
        }

    }
}
