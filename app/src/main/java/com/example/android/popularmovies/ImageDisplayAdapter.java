package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

/**
 * Created by Ben on 8/27/2017.
 */

public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageDisplayAdapter.ImageDisplayAdapterViewHolder>{
    private String[] mMovieImageResults;
    private Context context;
    private int imageWidth = 0;
    private int imageHeight = 0;

    public ImageDisplayAdapter(){

    }

    public void setImageData(String[] imageData){
        mMovieImageResults = imageData;
        notifyDataSetChanged();
    }

    public class ImageDisplayAdapterViewHolder extends RecyclerView.ViewHolder{
        public final ImageView mImageDisplay;

        public ImageDisplayAdapterViewHolder(View view) {
            super(view);
            mImageDisplay = (ImageView) view.findViewById(R.id.imageDisplay);
        }
    }
    @Override
    public ImageDisplayAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForImageDisplayItem = R.layout.image_display_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForImageDisplayItem, parent, shouldAttachToParentImmediately);
        return new ImageDisplayAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageDisplayAdapterViewHolder holder, int position) {
        Log.d(TAG, "width and height: " + imageWidth + " " + imageHeight);
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

    public void setImageSize(int width, int height){
        imageWidth = width;
        imageHeight = height;
    }
}
