package org.nanodegree.android.krafla.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.nanodegree.android.krafla.popularmovies.data.Constants;
import org.nanodegree.android.krafla.popularmovies.data.JsonUtil;
import org.nanodegree.android.krafla.popularmovies.data.Movie;
import org.nanodegree.android.krafla.popularmovies.data.Review;
import org.nanodegree.android.krafla.popularmovies.data.Trailer;
import org.nanodegree.android.krafla.popularmovies.data.URLs;
import org.nanodegree.android.krafla.popularmovies.db.MovieContract;

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
import static org.nanodegree.android.krafla.popularmovies.data.Constants.RATED;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.RELEASED;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.REVIEWS_LIST_KEY;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.TRAILERS_LIST_KEY;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public static final String YOUTUBE_STRING = "https://www.youtube.com/watch?v=";
    private ArrayList<Trailer> trailers;
    private LinearLayout trailerView;
    private LinearLayout reviewView;
    private Movie movie;
    private ArrayList<Review> reviews;

    public DetailActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (trailers != null) {
            outState.putParcelableArrayList(TRAILERS_LIST_KEY, trailers);
        }
        if (movie != null) {
            outState.putParcelable(Constants.MOVIE_KEY, movie);
        }
        if (reviews != null) {
            outState.putParcelableArrayList(Constants.REVIEWS_LIST_KEY, reviews);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        trailerView = (LinearLayout) rootView.findViewById(R.id.trailer_cards);
        reviewView = (LinearLayout) rootView.findViewById(R.id.review_list);

        if (savedInstanceState != null) {
            processSavedInstanceState(savedInstanceState, rootView);
        }

        Activity activity = getActivity();
        Intent intent = activity.getIntent();
        if (intent != null && intent.hasExtra(MOVIE_KEY)) {
            Bundle bundleExtra = intent.getBundleExtra(MOVIE_KEY);
            movie = Movie.parseFromBundle(bundleExtra);
            update(rootView);
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(MOVIE_KEY);
            update(rootView);
        }

        final ImageButton favouritesButton = (ImageButton) rootView.findViewById(R.id.add_to_favourites);
        final Bitmap greyHeart = BitmapFactory.decodeResource(getResources(), R.drawable.grey_heart);
        Bitmap redHeart = BitmapFactory.decodeResource(getResources(), R.drawable.red_heart);


        if (movie == null) {
            return rootView;
        }

        final boolean favourite = movie.isFavourite(getActivity());
        favouritesButton.setImageBitmap(favourite ? redHeart : greyHeart);

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // un-favourite the movie
                if (favourite) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            getActivity().getContentResolver().delete(
                                    MovieContract.MovieEntry.CONTENT_URI,
                                    MovieContract.MovieEntry.MOVIE_ID + " = ?",
                                    new String[]{Integer.toString(movie.getId())}
                            );
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            favouritesButton.setImageBitmap(greyHeart);
                        }
                    }.execute();
                } else {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            ContentValues values = new ContentValues();

                            values.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
                            values.put(MovieContract.MovieEntry.ORIGINAL_TITLE, movie.getOriginalTitle());
                            values.put(MovieContract.MovieEntry.POSTER, movie.getPosterPath());
                            values.put(MovieContract.MovieEntry.BACKDROP, movie.getBackdropPath());
                            values.put(MovieContract.MovieEntry.OVERVIEW, movie.getOverview());
                            values.put(MovieContract.MovieEntry.RATING, movie.getUserRating());
                            values.put(MovieContract.MovieEntry.RELEASE_DATE, movie.getReleaseDate());

                            getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            favouritesButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red_heart));
                        }
                    }.execute();
                }
            }
        });

        return rootView;
    }

    public class FetchTrailersTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String trailerUrlPattern = URLs.TRAILER_URL_PATTERN;
            return getServerAnswer(trailerUrlPattern, params);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(LOG_TAG, "returned trailers string = " + s);
            try {
                trailers = JsonUtil.parseTrailerString(s);
                processTrailers();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Could not parse string returned from the server", e);
            }
        }
    }

    private class FetchReviewsTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String trailerUrlPattern = URLs.REVIEWS_URL_PATTERN;
            return getServerAnswer(trailerUrlPattern, params);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(LOG_TAG, "returned reviews string = " + s);
            try {
                reviews = JsonUtil.parseReviewString(s);
                processReviews();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Could not parse string returned from the server", e);
            }
        }
    }

    @Nullable
    private String getServerAnswer(String urlPattern, Integer[] params) {
        if (params.length < 1) {
            throw new IllegalArgumentException("Bad params");
        }

        int id = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String answerString = null;
        try {
            Uri builtUri = Uri.parse(MessageFormat.format(urlPattern, String.valueOf(id)))
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

    private void processTrailers() {
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
    }

    private void processReviews() {
        for (int i = 0; i < reviews.size(); i++) {
            final Review review = reviews.get(i);

            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View reviewItemView = inflater.inflate(R.layout.review_item, null);
            TextView reviewAuthor = (TextView) reviewItemView.findViewById(R.id.author);
            TextView reviewContent = (TextView) reviewItemView.findViewById(R.id.content);

            reviewAuthor.setText(review.getAuthor());
            reviewContent.setText(review.getContent());

            reviewView.addView(reviewItemView);
        }
    }

    private void processSavedInstanceState(Bundle savedInstanceState, View rootView) {
        if (savedInstanceState != null) {
            trailers = savedInstanceState.getParcelableArrayList(TRAILERS_LIST_KEY);
            movie = savedInstanceState.getParcelable(MOVIE_KEY);
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS_LIST_KEY);
            processTrailers();
            processReviews();
        }
        update(rootView);
    }

    private void update(View rootView) {
        int movieId = movie.getId();
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_title);
        titleView.setText(movie.getOriginalTitle());

        ImageView backdropView = (ImageView) rootView.findViewById(R.id.backdrop);
        Picasso.with(getActivity()).load(movie.getUriForBackdrop(DEFAULT_BACKDROP_SIZE)).into(backdropView);

        TextView synopsisView = (TextView) rootView.findViewById(R.id.synopsis);
        synopsisView.setText(movie.getOverview() == null ? "" : movie.getOverview());

        TextView releaseView = (TextView) rootView.findViewById(R.id.released);
        releaseView.setText(RELEASED + movie.getReleaseYear());

        TextView ratedView = (TextView) rootView.findViewById(R.id.rated);
        ratedView.setText(RATED + movie.getUserRating());
        if (trailers == null) {
            new FetchTrailersTask().execute(movieId);
        }
        if (reviews == null) {
            new FetchReviewsTask().execute(movieId);
        }
    }

    private static String createYouTubeLink(String key) {
        return YOUTUBE_STRING + key;
    }
}
