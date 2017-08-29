package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    public static final String TAG = "DetailActivity";
    private ImageView mPoster;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mSynopsis;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = (ImageView) findViewById(R.id.Poster);
        mRating = (TextView) findViewById(R.id.Rating);
        mReleaseDate = (TextView) findViewById(R.id.Release_Date);
        mSynopsis = (TextView) findViewById(R.id.Synopsis);
        mTitle = (TextView) findViewById(R.id.Title);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movieData")) {
            Log.d(TAG, "onCreate: Intent started");
            String[] movieData = intentThatStartedThisActivity.getStringArrayExtra("movieData");

            // 1 - imageURL, 2 - title, 3 - release date, 4 - rating, 5 - synopsis
            Picasso.with(this).load(movieData[0]).fit().into(mPoster);
            mTitle.setText(movieData[1]);
            mReleaseDate.setText("Release Date :" + movieData[2]);

            if (movieData[3].matches("0")) {
                mRating.setText("No Rating");
            } else mRating.setText("Rating: " +movieData[3] + "/10");

            if (movieData[4].matches("")){
                mSynopsis.setText("No synopsis given.");
            } else mSynopsis.setText(movieData[4]);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
