package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ben on 9/5/2017.
 */

public class Movie implements Parcelable {
    String name;
    int id;
    String synopsis;
    String imageURL;
    String date;
    Double rating;

    public Movie(String name, int id, String synopsis, String imageURL, String date, Double rating){
        this.name = name;
        this.id = id;
        this.synopsis = synopsis;
        this.imageURL = imageURL;
        this.date = date;
        this.rating = rating;
    }

    public Movie(Parcel in){
        this.name = in.readString();
        this.id = in.readInt();
        this.synopsis = in.readString();
        this.imageURL = in.readString();
        this.date = in.readString();
        this.rating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeString(synopsis);
        dest.writeString(imageURL);
        dest.writeString(date);
        dest.writeDouble(rating);
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
