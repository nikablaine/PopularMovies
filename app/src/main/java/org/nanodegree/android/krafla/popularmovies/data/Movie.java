package org.nanodegree.android.krafla.popularmovies.data;

import android.net.Uri;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import static org.nanodegree.android.krafla.popularmovies.data.Constants.EMPTY_STRING;
import static org.nanodegree.android.krafla.popularmovies.data.URLs.POSTER_BASE_URL;

/**
 * Class representing film.
 */
public class Movie {

    public static final String ORIGINAL_TITLE_KEY = "original_title";
    public static final String POSTER_PATH_KEY = "poster_path";
    public static final String BACKDROP_PATH_KEY = "backdrop_path";
    public static final String OVERVIEW_KEY = "overview";
    public static final String VOTE_AVERAGE_KEY = "vote_average";
    public static final String RELEASE_DATE_KEY = "release_date";
    /**
     * Original title of a movie.
     */
    private String originalTitle;

    /**
     * Path to the image with movie poster.
     */
    private String posterPath;

    /**
     * Path to the backdrop image.
     */
    private String backdropPath;

    /**
     * Plot synopsis of a movie.
     */
    private String overview;

    /**
     * User rating of a movie (called vote_average).
     */
    private double userRating;


    /**
     * Release date of a movie.
     */
    private String releaseDate;


    public static Movie parseFromJSONObject(JSONObject movieObject) throws JSONException {
        String originalTitle = movieObject.getString(ORIGINAL_TITLE_KEY);
        String posterPath = movieObject.getString(POSTER_PATH_KEY);
        String backdropPath = movieObject.getString(BACKDROP_PATH_KEY);
        String overview = movieObject.getString(OVERVIEW_KEY);
        double userRating = movieObject.getDouble(VOTE_AVERAGE_KEY);
        String releaseDate = movieObject.getString(RELEASE_DATE_KEY);

        return new MovieBuilder()
                .setOriginalTitle(originalTitle)
                .setPosterPath(posterPath)
                .setBackdropPath(backdropPath)
                .setOverview(overview)
                .setUserRating(userRating)
                .setReleaseDate(releaseDate)
                .createMovie();
    }

    public static Movie parseFromBundle(Bundle bundle) {
        String originalTitle = bundle.getString(ORIGINAL_TITLE_KEY);
        String posterPath = bundle.getString(POSTER_PATH_KEY);
        String backdropPath = bundle.getString(BACKDROP_PATH_KEY);
        String overview = bundle.getString(OVERVIEW_KEY);
        double userRating = bundle.getDouble(VOTE_AVERAGE_KEY);
        String releaseDate = bundle.getString(RELEASE_DATE_KEY);

        return new MovieBuilder()
                .setOriginalTitle(originalTitle)
                .setPosterPath(posterPath)
                .setBackdropPath(backdropPath)
                .setOverview(overview)
                .setUserRating(userRating)
                .setReleaseDate(releaseDate)
                .createMovie();
    }

    public Movie(String originalTitle, String posterPath, String backdropPath, String overview, double userRating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    /**
     * Returns Uri for the movie poster that can later be used by Picasso to load the image.
     *
     * @param posterSize poster size to specify
     * @return built Uri
     */
    public Uri getUriForPoster(String posterSize) {
        return Uri.parse(POSTER_BASE_URL + posterSize + posterPath);
    }

    /**
     * Returns Uri for the backdrop image..
     *
     * @param posterSize poster size to specify
     * @return built Uri
     */
    public Uri getUriForBackdrop(String posterSize) {
        return Uri.parse(POSTER_BASE_URL + posterSize + backdropPath);
    }

    /**
     * Creates a bundle with all the information about movie.
     *
     * @return bundle with movie information
     */
    public Bundle assembleBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ORIGINAL_TITLE_KEY, originalTitle);
        bundle.putString(POSTER_PATH_KEY, posterPath);
        bundle.putString(BACKDROP_PATH_KEY, backdropPath);
        bundle.putString(OVERVIEW_KEY, overview);
        bundle.putDouble(VOTE_AVERAGE_KEY, userRating);
        bundle.putString(RELEASE_DATE_KEY, releaseDate);
        return bundle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getUserRating() {
        return userRating;
    }

    /**
     * Returns the year of a movie release.
     *
     * @return release year
     */
    public String getReleaseYear() {
        if (releaseDate != null || releaseDate.length() >= 4) {
            return releaseDate.substring(0, 4);
        }
        return EMPTY_STRING;
    }
}
