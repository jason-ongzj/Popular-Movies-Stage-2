package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.CustomCursorAdapterViewHolder> {

    private Cursor mCursor;
    private final Context mContext;

    final private CustomCursorAdapterOnClickHandler mClickHandler;

    public interface CustomCursorAdapterOnClickHandler{
        void onCustomCursorAdapterImageClicked(Movie movie);
    }

    public CustomCursorAdapter(CustomCursorAdapterOnClickHandler onClickHandler, Context context){
        mClickHandler = onClickHandler;
        mContext = context;
    }

    public void setCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public class CustomCursorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mImageDisplay;

        public CustomCursorAdapterViewHolder(View view) {
            super(view);
            mImageDisplay = (ImageView) view.findViewById(R.id.imageDisplay);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onCustomCursorAdapterImageClicked(returnMovie());
        }

        private Movie returnMovie(){
            mCursor.moveToPosition(getAdapterPosition());
            Long movie_id = mCursor.getLong(MainActivity.INDEX_MOVIE_ID);
            String movie_name = mCursor.getString(MainActivity.INDEX_MOVIE_NAME);
            String movie_imageURL = mCursor.getString(MainActivity.INDEX_MOVIE_IMAGE_URL);
            String movie_synopsis = mCursor.getString(MainActivity.INDEX_MOVIE_SYNOPSIS);
            String movie_date = mCursor.getString(MainActivity.INDEX_MOVIE_RELEASE_DATE);
            Double movie_rating = mCursor.getDouble(MainActivity.INDEX_MOVIE_RATING);

            return new Movie(movie_name, movie_id, movie_synopsis, movie_imageURL, movie_date, movie_rating, Movie.TRUE);
        }
    }

    @Override
    public CustomCursorAdapter.CustomCursorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForImageDisplayItem = R.layout.image_display_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForImageDisplayItem, parent, false);
        return new CustomCursorAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomCursorAdapter.CustomCursorAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String imageURL = mCursor.getString(MainActivity.INDEX_MOVIE_IMAGE_URL);
        Picasso.with(mContext).load(imageURL).fit().into(holder.mImageDisplay);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }
}
