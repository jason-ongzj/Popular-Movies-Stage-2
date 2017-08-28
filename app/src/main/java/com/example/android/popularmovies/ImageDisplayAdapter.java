package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Ben on 8/27/2017.
 */

public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageDisplayAdapter.ImageDisplayAdapterViewHolder>{
    private String[] mMovieImageResults;
    private String[] mMovieTitles;
    private String[] mReleaseDates;
    private String[] mSynopsis;
    private String[] mVoteAverage;
    private Context context;
    private int imageWidth = 0;
    private int imageHeight = 0;

    final private ImageDisplayAdapterOnClickHandler mClickHandler;

    public interface ImageDisplayAdapterOnClickHandler{
        //Pass movie data array
        void onDisplayImageClicked(String[] movieData);
    }

    public ImageDisplayAdapter(ImageDisplayAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public void setImageData(String[] imageData){
        mMovieImageResults = imageData;
        notifyDataSetChanged();
    }

    public void setTitle(String[] titles){
        mMovieTitles = titles;
        notifyDataSetChanged();
    }

    public void setReleaseDates(String[] releaseDates){
        mReleaseDates = releaseDates;
        notifyDataSetChanged();
    }

    public void setSynopsis(String[] synopses){
        mSynopsis = synopses;
        notifyDataSetChanged();
    }

    public void setVoteAverage(String[] voteAverages){
        mVoteAverage = voteAverages;
        notifyDataSetChanged();
    }

    public class ImageDisplayAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mImageDisplay;

        public ImageDisplayAdapterViewHolder(View view) {
            super(view);
            mImageDisplay = (ImageView) view.findViewById(R.id.imageDisplay);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, mMovieTitles[getAdapterPosition()], Toast.LENGTH_SHORT).show();
            if (mMovieTitles != null)
                mClickHandler.onDisplayImageClicked(returnMovieData());
        }

        // Pass data through onClick listener to intent
        private String[] returnMovieData(){
            String[] movieData = new String[5];

            movieData[0] = mMovieImageResults[getAdapterPosition()];
            movieData[1] = mMovieTitles[getAdapterPosition()];
            movieData[2] = mReleaseDates[getAdapterPosition()];
            movieData[3] = mVoteAverage[getAdapterPosition()];
            movieData[4] = mSynopsis[getAdapterPosition()];

            return movieData;
        }
    }

    @Override
    public ImageDisplayAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForImageDisplayItem = R.layout.image_display_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForImageDisplayItem, parent, false);
        return new ImageDisplayAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageDisplayAdapterViewHolder holder, int position) {
        String imageForMovie = mMovieImageResults[position];
        holder.mImageDisplay.setVisibility(View.VISIBLE);
        if (context != null)
            Picasso.with(context).load(imageForMovie).fit().into(holder.mImageDisplay);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieImageResults) return 0;
        return mMovieImageResults.length;
    }

    public void setContext(Context mainContext){
        context = mainContext;
    }
}
