package com.benthedeveloper.popmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        FetchMovieIdsTask movieIdsTask = new FetchMovieIdsTask();
        // Add sharedpreferences stuff here instead of hardcoding sorting parameter
        String sortingParamValue = getString(R.string.api_parameter_value_popularitydesc);
        movieIdsTask.execute(sortingParamValue);

        // TODO: Get movie poster URL from ids 

        // End test getting some data using theMovieDB

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    /**
     * Take the movies data string in JSON Format and get an array of movie IDs we can use to
     * get the poster, title, etc.
     */
    private int[] getMovieIdsFromJson(String moviesJsonStr)
            throws JSONException {

        final String OMD_RESULTS = "results";
        final String OMD_ID = "id";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(OMD_RESULTS);

        int[] movieIdsArray = new int[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            int curId;

            // Get the JSON object representing the movie
            JSONObject movieObj = moviesArray.getJSONObject(i);
            curId = movieObj.getInt(OMD_ID);
            movieIdsArray[i] = curId;
        }

        // TEST LOG
        Log.v(LOG_TAG, movieIdsArray.toString());

        return movieIdsArray;
    }

    /**
     * Class to get the data from theMovieDB.org API on background thread
     */
    public class FetchMovieIdsTask extends AsyncTask<String, Void, int[]> {

        private final String LOG_TAG = FetchMovieIdsTask.class.getSimpleName();

        @Override
        protected int[] doInBackground(String... params) {

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
                //return builder.build().toString();
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
                // If the code didn't successfully get the weather data, there's no point in attemping
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
                return getMovieIdsFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the data
            return null;
        }// end doInBackground method

        // Update the UI here
        @Override
        protected void onPostExecute(int[] result) {
            if (result != null) {
                for (int movieId : result) {
                    Log.v("onPostExecute", new Integer(movieId).toString());
                }
//                mForecastAdapter.clear();
//                for(String dayForecastStr : result) {
//                    mForecastAdapter.add(dayForecastStr);
//                }
                // New data is back from the server.  Hooray!
            }
        }
    }
    // end FetchMovieIdsTask class
}
