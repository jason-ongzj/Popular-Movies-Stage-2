package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    final String name;
    final long id;
    final String synopsis;
    final String imageURL;
    final String date;
    final Double rating;
    int favourite; // 0 - false, 1 - true

    public Movie(String name, long id, String synopsis, String imageURL, String date, Double rating, int favourite){
        this.name = name;
        this.id = id;
        this.synopsis = synopsis;
        this.imageURL = imageURL;
        this.date = date;
        this.rating = rating;
        this.favourite = favourite;
    }

    private Movie(Parcel in){
        this.name = in.readString();
        this.id = in.readLong();
        this.synopsis = in.readString();
        this.imageURL = in.readString();
        this.date = in.readString();
        this.rating = in.readDouble();
        this.favourite = in.readInt();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(id);
        dest.writeString(synopsis);
        dest.writeString(imageURL);
        dest.writeString(date);
        dest.writeDouble(rating);
        dest.writeInt(favourite);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
