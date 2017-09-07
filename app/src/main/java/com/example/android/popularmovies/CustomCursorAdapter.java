package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ben on 9/7/2017.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.CustomCursorAdapterViewHolder> {

    @Override
    public CustomCursorAdapter.CustomCursorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomCursorAdapter.CustomCursorAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomCursorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CustomCursorAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
