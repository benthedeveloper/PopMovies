package com.benthedeveloper.popmovies;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ben Harrison on 1/19/2016.
 * Object to represent a movie for the app
 */
public class Movie {
    private int id;
    private String originalTitle;
    private String posterURL;
    private String overview;
    private double voteAverage;
    private Date releaseDate;

    // Constructor
    public Movie(int id, String originalTitle, String posterURL, String overview, double voteAverage, String releaseDateStr) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterURL = posterURL;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = formatDateString(releaseDateStr, "yyyy-MM-dd");
    }

    // toString method
    public String toString() {
        return "id: " + getId() + "\n"
                + "originalTitle: " + getOriginalTitle() + "\n"
                + "posterURL: " + getPosterURL() + "\n"
                + "overview: " + getOverview() + "\n"
                + "voteAverage: " + getVoteAverage() + "\n"
                + "releaseDate: " + getDateString(getReleaseDate(), "yyyy-MM-dd") + "\n\n";
    }

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

    public double getVoteAverage() {
        return this.voteAverage;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    // helper method to convert string to a Date object
    private Date formatDateString(String dateStr, String formatStr) {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        try {
            Date releaseDateObj = dateFormat.parse(dateStr);
            return releaseDateObj;
        } catch(Exception e) {
            Log.e("Error", "Error parsing date");
        }
        // If we got here there was an error parsing date
        return null;
    }

    // helper method to convert Date object to string
    private String getDateString(Date dateObj, String formatStr) {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        return dateFormat.format(dateObj);
    }
}
