package com.benthedeveloper.popmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Test getting some data using theMovieDB
        String mostPopularMoviesURL = getMovieDataURL(getString(R.string.api_parameter_value_popularitydesc));
        String highestRatedMoviesURL = getMovieDataURL(getString(R.string.api_parameter_value_voteaveragedesc));
        Log.v(LOG_TAG, "most popular URL: " + mostPopularMoviesURL);
        Log.v(LOG_TAG, "highest rated URL: " + highestRatedMoviesURL);
        // End test getting some data using theMovieDB

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // Helper method to get Movie URL
    private String getMovieDataURL(String sortByParamValue) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.api_scheme))
                .authority(getString(R.string.api_authority))
                .appendPath(getString(R.string.api_version))
                .appendPath(getString(R.string.api_path_discover))
                .appendPath(getString(R.string.api_path_movie))
                .appendQueryParameter(getString(R.string.api_parameter_key_sortby), sortByParamValue)
                .appendQueryParameter(getString(R.string.api_parameter_key_apikey), BuildConfig.THE_MOVIE_DB_API_TOKEN);
        return builder.build().toString();
    }
}
