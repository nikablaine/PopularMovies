package org.nanodegree.android.krafla.popularmovies.data;

import org.nanodegree.android.krafla.popularmovies.db.MovieContract;

/**
 * Utility class containing useful constant values.
 *
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class Constants {

    public static final String DEFAULT_POSTER_SIZE = "w185";
    public static final String DEFAULT_BACKDROP_SIZE = "w500";
    public static final String RELEASED = "Released: ";
    public static final String RATED = "Rated: ";
    public static final String MOVIE_KEY = "movie";
    public static final String SORT_PARAM = "sort_by";
    public static final String API_KEY_PARAM = "api_key";
    public static final String POPULARITY_OPTION = "popularity.desc";
    public static final String RATING_OPTION = "vote_average.desc";
    public static final String EMPTY_STRING = "";
    public static final String MOVIES_LIST_KEY = "movies";
    public static final String TRAILERS_LIST_KEY = "trailers";
    public static final String REVIEWS_LIST_KEY = "reviews";
    public static final String POPULARITY_KEY = "pop";
    public static final String MOVIE_ID_KEY = "movie_id";
    public static final String SEPARATOR = ": ";

    public static final String END_OF_LINE = "; ";
    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_POSTER = 3;
    public static final int COL_BACKDROP = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_RATING = 6;

    public static final int COL_RELEASED = 7;
    public static final String[] MOVIE_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.MOVIE_ID,
            MovieContract.MovieEntry.ORIGINAL_TITLE,
            MovieContract.MovieEntry.POSTER,
            MovieContract.MovieEntry.BACKDROP,
            MovieContract.MovieEntry.OVERVIEW,
            MovieContract.MovieEntry.RATING,
            MovieContract.MovieEntry.RELEASE_DATE
    };

    public enum Sort {
        POPULARITY,
        RATING,
        FAVOURITES
    }

    private Constants() {
    }
}
