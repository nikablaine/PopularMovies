package org.nanodegree.android.krafla.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.nanodegree.android.krafla.popularmovies.data.Constants;
import org.nanodegree.android.krafla.popularmovies.data.JsonUtil;
import org.nanodegree.android.krafla.popularmovies.data.Movie;
import org.nanodegree.android.krafla.popularmovies.data.Trailer;
import org.nanodegree.android.krafla.popularmovies.data.URLs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;

import static org.nanodegree.android.krafla.popularmovies.data.Constants.API_KEY_PARAM;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.DEFAULT_BACKDROP_SIZE;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.MOVIE_KEY;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public static final String YOUTUBE_STRING = "https://www.youtube.com/watch?v=";
    private LinearLayout trailerView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Activity activity = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        trailerView = (LinearLayout) rootView.findViewById(R.id.trailer_cards);

        // normally, we will open the detail activity passing along the movie information
        Intent intent = activity.getIntent();

        if (intent != null && intent.hasExtra(MOVIE_KEY)) {
            Bundle bundleExtra = intent.getBundleExtra(MOVIE_KEY);
            Movie movie = Movie.parseFromBundle(bundleExtra);

            TextView titleView = (TextView) rootView.findViewById(R.id.movie_title);
            titleView.setText(movie.getOriginalTitle());

            ImageView backdropView = (ImageView) rootView.findViewById(R.id.backdrop);
            Picasso.with(activity).load(movie.getUriForBackdrop(DEFAULT_BACKDROP_SIZE)).into(backdropView);

            TextView synopsisView = (TextView) rootView.findViewById(R.id.synopsis);
            synopsisView.setText(movie.getOverview() == null ? "" : movie.getOverview());

            TextView releaseView = (TextView) rootView.findViewById(R.id.released);
            releaseView.setText(Constants.RELEASED + movie.getReleaseYear());

            TextView ratedView = (TextView) rootView.findViewById(R.id.rated);
            ratedView.setText(Constants.RATED + movie.getUserRating());

            new FetchTrailersTask().execute(movie.getId());
        }

        return rootView;
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length < 1) {
                throw new IllegalArgumentException("Bad params");
            }

            String id = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String answerString = null;
            try {
                Uri builtUri = Uri.parse(MessageFormat.format(URLs.TRAILER_URL_PATTERN, id))
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, getResources().getString(R.string.api_key))
                        .build();
                URL url = new URL(builtUri.toString());

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
            Log.d(LOG_TAG, "returned string = " + s);
            try {
                ArrayList<Trailer> trailers = JsonUtil.parseTrailerString(s);
                for (int i = 0; i < trailers.size(); i++) {
                    final Trailer trailer = trailers.get(i);

                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View trailerItemView = inflater.inflate(R.layout.trailer_item, null);
                    TextView trailerName = (TextView) trailerItemView.findViewById(R.id.trailer_name);
                    trailerName.setText(trailer.getName());

                    trailerView.addView(trailerItemView);
                    trailerItemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(createYouTubeLink(trailer.getKey()))));
                        }
                    });
                }
                Log.d(LOG_TAG, "trailers = " + trailers);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Could not parse string returned from the server", e);
            }
        }
    }

    private static String createYouTubeLink(String key) {
        return YOUTUBE_STRING + key;
    }
}
