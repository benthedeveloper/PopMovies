package com.benthedeveloper.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Main Activity fragment
 */
public class MainActivityFragment extends Fragment {

    private MoviesAdapter mMoviesAdapter;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source and
        // use it to populate the view it's attached to.
        mMoviesAdapter = new MoviesAdapter(getContext(), new ArrayList<Movie>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach the adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridlayout_popmovies);
        gridView.setAdapter(mMoviesAdapter);
        // Set click listener for gridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie clickedMovie = mMoviesAdapter.getItem(position);
                // Pass Movie object to DetailActivity in an Intent
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movie_object", clickedMovie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * Method to update the main UI
     */
    private void updateMoviesBasic() {
        FetchMoviesBasicTask moviesTask = new FetchMoviesBasicTask();
        // Add sharedpreferences stuff here instead of hardcoding sorting parameter
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingParamValue = prefs.getString(getString(R.string.pref_sortby_key),
                getString(R.string.pref_sortby_default));
        moviesTask.execute(sortingParamValue);
    }

    /**
     * Update the basic UI when the activity starts
     */
    @Override
    public void onStart() {
        super.onStart();
        updateMoviesBasic();
    }

    /**
     * Method to get the full poster URL for a movie, given its poster_path
     */
    private String getMoviePosterURL(String posterPath) {
        // construct URL for theMovieDB API query (example URL: http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.api_scheme))
                .authority(getString(R.string.api_image_base_url))
                .appendPath("t")
                .appendPath("p")
                .appendPath(getString(R.string.api_poster_size_phone))
                .appendEncodedPath(posterPath)
                .appendQueryParameter(getString(R.string.api_parameter_key_apikey), BuildConfig.THE_MOVIE_DB_API_TOKEN);
        String imageURLString = builder.build().toString();

        return imageURLString;
    }

    /**
     * Class to get the data from theMovieDB.org API on background thread
     */
    public class FetchMoviesBasicTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesBasicTask.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            // If there's no sort_by parameter, there's nothing to look up. Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // Get the sorting parameter passed in
            String sortByParamValue = params[0];

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                // construct URL for theMovieDB API query
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(getString(R.string.api_scheme))
                        .authority(getString(R.string.api_authority))
                        .appendPath(getString(R.string.api_version))
                        .appendPath(getString(R.string.api_path_discover))
                        .appendPath(getString(R.string.api_path_movie))
                        .appendQueryParameter(getString(R.string.api_parameter_key_sortby), sortByParamValue)
                        .appendQueryParameter(getString(R.string.api_parameter_key_apikey), BuildConfig.THE_MOVIE_DB_API_TOKEN);
                String urlString = builder.build().toString();
                URL url = new URL(urlString);

                // Create the request to theMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                final String OMD_RESULTS = "results";

                JSONObject moviesJson = new JSONObject(moviesJsonStr);
                JSONArray moviesJSONArray = moviesJson.getJSONArray(OMD_RESULTS);

                // ArrayList to store the result in
                ArrayList<Movie> result = new ArrayList<Movie>(moviesJSONArray.length());

                // Populate result with each Movie object
                for (int i = 0; i < moviesJSONArray.length(); i++) {
                    // Get the JSON object representing the movie
                    JSONObject movieObj = moviesJSONArray.getJSONObject(i);
                    int id = movieObj.getInt("id");
                    String originalTitle = movieObj.getString("original_title");
                    String posterURL = getMoviePosterURL(movieObj.getString("poster_path"));
                    String overview = movieObj.getString("overview");
                    double voteAverage = movieObj.getDouble("vote_average");
                    String releaseDateStr = movieObj.getString("release_date");
                    // Add the Movie object
                    result.add(new Movie(id, originalTitle, posterURL, overview, voteAverage, releaseDateStr));
                }

                // return the ArrayList containing Movie objects
                return result;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the data
            return null;
        }// end doInBackground method

        // Update the UI here
        @Override
        protected void onPostExecute(ArrayList<Movie> moviesArrayList) {
            if (moviesArrayList != null) {
                mMoviesAdapter.clear();
                for (Movie movie : moviesArrayList) {
                    mMoviesAdapter.add(movie);
                }
            }
        }
    }
}
