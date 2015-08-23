package org.nanodegree.android.krafla.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nanodegree.android.krafla.popularmovies.data.Constants;
import org.nanodegree.android.krafla.popularmovies.data.Movie;

import static org.nanodegree.android.krafla.popularmovies.data.Constants.DEFAULT_BACKDROP_SIZE;
import static org.nanodegree.android.krafla.popularmovies.data.Constants.MOVIE_KEY;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Activity activity = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // normally, we will open the detail activity passing along the movie information
        Intent intent = activity.getIntent();

        if (intent != null && intent.hasExtra(MOVIE_KEY)) {
            Bundle bundleExtra = intent.getBundleExtra(MOVIE_KEY);
            Movie movie = Movie.parseFromBundle(bundleExtra);

            TextView titleView = (TextView) rootView.findViewById(R.id.movie_title);
            titleView.setText(movie.getOriginalTitle());

            ImageView backdropview = (ImageView) rootView.findViewById(R.id.backdrop);
            Picasso.with(activity).load(movie.getUriForBackdrop(DEFAULT_BACKDROP_SIZE)).into(backdropview);

            TextView synopsisView = (TextView) rootView.findViewById(R.id.synopsis);
            synopsisView.setText(movie.getOverview() == null ? "" : movie.getOverview());

            TextView releaseView = (TextView) rootView.findViewById(R.id.released);
            releaseView.setText(Constants.RELEASED + movie.getReleaseYear());

            TextView ratedView = (TextView) rootView.findViewById(R.id.rated);
            ratedView.setText(Constants.RATED + movie.getUserRating());
        }

        return rootView;
    }
}
