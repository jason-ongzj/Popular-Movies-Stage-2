package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageDisplayAdapter.ImageDisplayAdapterViewHolder>{

    private ArrayList<Movie> mMovies;
    private Context mContext;

    final private ImageDisplayAdapterOnClickHandler mClickHandler;

    public interface ImageDisplayAdapterOnClickHandler{
        //Pass movie object instance
        void onDisplayImageClicked(Movie movie);
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public ImageDisplayAdapter(ImageDisplayAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
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
            Toast.makeText(mContext, mMovies.get(getAdapterPosition()).name, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onClick: " + mMovies.size());
            if (mMovies != null)
                mClickHandler.onDisplayImageClicked(returnMovie());
        }

        // Pass data through onClick listener to intent
        private Movie returnMovie(){
            mMovies.get(getAdapterPosition()).favourite = queryMovieInSQLDb(mMovies.get(getAdapterPosition()));
            return mMovies.get(getAdapterPosition());
        }
    }

    @Override
    public ImageDisplayAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForImageDisplayItem = R.layout.image_display_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForImageDisplayItem, parent, false);
        return new ImageDisplayAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageDisplayAdapterViewHolder holder, int position) {
        String imageURL = mMovies.get(position).imageURL;
        holder.mImageDisplay.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(imageURL).fit().into(holder.mImageDisplay);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    public void setContext(Context context){
        mContext = context;
    }

    private int queryMovieInSQLDb(Movie movie){
        Cursor c = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                "movie_id=?",
                new String[] {Long.toString(movie.id)},
                null);

        if (c.getCount() > 0) {
            c.close();
            return Movie.TRUE;
        } else {
            c.close();
            return Movie.FALSE;
        }
    }
}
