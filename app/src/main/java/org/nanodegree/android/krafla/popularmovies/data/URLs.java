package org.nanodegree.android.krafla.popularmovies.data;

/**
 * Utility class containing needed URLs.
 *
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class URLs {

    public static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";

    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    public static final String TRAILER_URL_PATTERN = "https://api.themoviedb.org/3/movie/{0}/videos";
    public static final String REVIEWS_URL_PATTERN = "https://api.themoviedb.org/3/movie/{0}/reviews";

    /**
     * Default private constructor to prevent class from instantiating.
     */
    private URLs() {
    }

}
