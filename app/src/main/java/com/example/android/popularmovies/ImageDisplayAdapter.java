package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ben on 8/27/2017.
 */

public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageDisplayAdapter.ImageDisplayAdapterViewHolder>{
    private String[] mMovieImageResults;

    public ImageDisplayAdapter(){

    }

    public void setImageData(String[] imageData){
        this.mMovieImageResults = imageData;
        notifyDataSetChanged();
    }

    public class ImageDisplayAdapterViewHolder extends RecyclerView.ViewHolder{
        public final TextView mImageDisplay;
        //    public final TextView mTextView;

        public ImageDisplayAdapterViewHolder(View view) {
            super(view);
            mImageDisplay = (TextView) view.findViewById(R.id.imageDisplay);
            //       mTextView = (TextView) view.findViewById(R.id.textOutput);
        }
    }
    @Override
    public ImageDisplayAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForImageDisplayItem = R.layout.image_display_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForImageDisplayItem, parent, shouldAttachToParentImmediately);
        return new ImageDisplayAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageDisplayAdapterViewHolder holder, int position) {
        String imageForMovie = mMovieImageResults[position];
        Uri imageUri = Uri.parse(imageForMovie);
        holder.mImageDisplay.setText(imageForMovie);
        holder.mImageDisplay.setVisibility(View.VISIBLE);
     //   if (context != null)
     //       Picasso.with(context).load(imageUri).into(holder.mImageDisplay);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieImageResults) return 0;
        return mMovieImageResults.length;
    }
}
