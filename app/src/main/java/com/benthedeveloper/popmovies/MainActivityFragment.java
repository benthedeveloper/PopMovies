package com.benthedeveloper.popmovies;

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
        String baseURL = "http://api.themoviedb.org/3/";
        String discoverMethodURL = "discover/movie";
        String mostPopularParam = "?sort_by=popularity.desc";
        String highestRatedParam = "?sort_by=vote_average.desc";
        String apiKeyParam = "&api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;
        Log.v(LOG_TAG, "most popular URL: " + baseURL + discoverMethodURL + mostPopularParam + apiKeyParam);
        Log.v(LOG_TAG, "highest rated URL: " + baseURL + discoverMethodURL + highestRatedParam + apiKeyParam);
        // End test getting some data using theMovieDB

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
