package org.nanodegree.android.krafla.popularmovies;

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

import org.json.JSONException;
import org.nanodegree.android.krafla.popularmovies.data.Constants;
import org.nanodegree.android.krafla.popularmovies.data.JsonUtil;
import org.nanodegree.android.krafla.popularmovies.data.Movie;
import org.nanodegree.android.krafla.popularmovies.data.URLs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.nanodegree.android.krafla.popularmovies.data.Constants.API_KEY_PARAM;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.MOVIE_KEY;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.POPULARITY_OPTION;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.RATING_OPTION;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.SORT_PARAM;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private ImageAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private boolean currentSort;

    public MainActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        // if we're resuming after changing a setting, then we update the grid view
        boolean sort = currentSort;
        updateCurrentSort();
        if (sort != currentSort) {
            updateMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (movies != null) {
            outState.putParcelableArrayList(Constants.MOVIES_LIST_KEY, movies);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        processSavedInstanceState(savedInstanceState);


        View view = inflater.inflate(R.layout.fragment_main, container, false);


        // find the grid view and set the adapter
        GridView gridView = (GridView) view.findViewById(R.id.movie_view);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new MovieItemClickListener());

        return view;
    }

    private void processSavedInstanceState(Bundle savedInstanceState) {
        // initialize the image adapter
        movieAdapter = new ImageAdapter(getActivity());

        if (savedInstanceState != null) {
            ArrayList<Movie> movieList = savedInstanceState.getParcelableArrayList(Constants.MOVIES_LIST_KEY);
            movieAdapter.updateData(movieList);
        } else {
            updateMovies();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void updateMovies() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        updateCurrentSort();
        fetchMovieTask.execute(currentSort);
    }

    private void updateCurrentSort() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String popularValue = getString(R.string.pref_sort_popular_value);
        String sort = defaultSharedPreferences.getString(getString(R.string.pref_sort_key), popularValue);
        currentSort = sort.equals(popularValue);
    }

    public class FetchMovieTask extends AsyncTask<Boolean, Void, String> {

        private final String apiKey = getString(R.string.api_key);

        @Override
        protected String doInBackground(Boolean... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String answerString = null;
            try {
                Uri builtUri = Uri.parse(URLs.MOVIEDB_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .appendQueryParameter(SORT_PARAM, params[0] ? POPULARITY_OPTION : RATING_OPTION)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.d(LOG_TAG, "Request = " + url);

                // Create the request to MovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
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
                answerString = buffer.toString();
                return answerString;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                movies = JsonUtil.parseMovieString(s);
                movieAdapter.updateData(movies);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Could not parse string returned from the server", e);
            }
        }
    }

    private class MovieItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Movie movie = (Movie) movieAdapter.getItem(position);
            Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
            detailIntent.putExtra(MOVIE_KEY, movie.assembleBundle());
            startActivity(detailIntent);
        }
    }
}
