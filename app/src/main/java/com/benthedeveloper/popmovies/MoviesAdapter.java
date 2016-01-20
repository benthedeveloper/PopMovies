package com.benthedeveloper.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ben Harrison on 1/20/2016.
 * Custom ArrayAdapter class
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

    // Constructor
    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_moviesbasic, parent, false);
        }
        // Lookup view for data population
        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.grid_item_moviesbasic_imageview);
        // Populate the data into the template view using the data object
        Picasso.with(getContext()).load(movie.getPosterURL()).into(posterImageView);
        // Return the completed view to render on screen
        return convertView;
    }
}
