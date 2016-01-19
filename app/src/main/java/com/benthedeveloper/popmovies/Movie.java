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
    private long voteAverage;
    private Date releaseDate;

    // Constructor
    public Movie(int id, String originalTitle, String posterURL, String overview, long voteAverage, String releaseDateStr) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterURL = posterURL;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = formatDateString(releaseDateStr);
    }

    // Method to get the movie's id
    public int getId() {
        return this.id;
    }

    // Method to get the movie's title
    public String getOriginalTitle() {
        return this.originalTitle;
    }

    // Method to get the movie's poster URL as a string
    public String getPosterURL() {
        return this.posterURL;
    }

    // helper method to convert string to a Date object
    private Date formatDateString(String dateStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
            Date releaseDateObj = dateFormat.parse(dateStr);
            return releaseDateObj;
        } catch(Exception e) {
            Log.e("Error", "Error parsing date");
        }
        // If we got here there was an error parsing date
        return null;
    }
}
