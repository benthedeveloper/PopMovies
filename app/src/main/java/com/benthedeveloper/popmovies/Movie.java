package com.benthedeveloper.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ben Harrison on 1/19/2016.
 * Object to represent a movie for the app
 */
public class Movie implements Parcelable {
    private int id;
    private String originalTitle;
    private String posterURL;
    private String overview;
    private double voteAverage;
    private String releaseDateStr;

    // Constructor
    public Movie(int id, String originalTitle, String posterURL, String overview, double voteAverage, String releaseDateStr) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterURL = posterURL;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDateStr = releaseDateStr;
    }

    // toString method
    @Override
    public String toString() {
        return "id: " + getId() + "\n"
                + "originalTitle: " + getOriginalTitle() + "\n"
                + "posterURL: " + getPosterURL() + "\n"
                + "overview: " + getOverview() + "\n"
                + "voteAverage: " + getVoteAverage() + "\n"
                + "releaseDateStr: " + getReleaseDateStr() + "\n\n";
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public String getPosterURL() {
        return this.posterURL;
    }

    public String getOverview() {
        return this.overview;
    }

    public double getVoteAverage() { return this.voteAverage; }

    public String getReleaseDateStr() {
        if (this.releaseDateStr == null || this.releaseDateStr.trim() == "") {
            return "";
        }
        return this.releaseDateStr;
    }


    protected Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        posterURL = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDateStr = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(posterURL);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDateStr);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
