package com.benthedeveloper.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Activity to show details for a Movie
 */
public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment for Movie details
     */
    public static class DetailFragment extends Fragment {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("movie_object")) {
                Movie movieObj = (Movie) intent.getExtras().getParcelable("movie_object");
                // Get the views to populate
                TextView movieTitleTextView = (TextView) rootView.findViewById(R.id.movieTitle_toolbar_textView);
                ImageView moviePosterImageView = (ImageView) rootView.findViewById(R.id.detail_moviePoster_imageView);
                TextView movieYearTextView = (TextView) rootView.findViewById(R.id.detail_movieYear_textView);
                TextView movieDurationTextView = (TextView) rootView.findViewById(R.id.detail_movieDuration_textView);
                TextView movieVoteAvgTextView = (TextView) rootView.findViewById(R.id.detail_movieVoteAvg_textView);
                TextView movieOverviewTextView = (TextView) rootView.findViewById(R.id.detail_overview_textView);
                // Set text and image values
                movieTitleTextView.setText(movieObj.getOriginalTitle());
                Picasso.with(getContext())
                        .load(movieObj.getPosterURL())
                        .fit()
                        .centerCrop()
                        .into(moviePosterImageView);
                movieYearTextView.setText(movieObj.getReleaseDateYear());
                movieDurationTextView.setText(movieObj.getRuntime() + getString(R.string.minutes_shorthand));
                movieVoteAvgTextView.setText(movieObj.getVoteAverage() + "/" + getString(R.string.max_vote_avg));
                movieOverviewTextView.setText(movieObj.getOverview());
            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.menu_main, menu);
        }
    }
}