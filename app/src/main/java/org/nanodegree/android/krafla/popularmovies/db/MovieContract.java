package org.nanodegree.android.krafla.popularmovies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "org.nanodegree.android.krafla.popularmovies.db.MovieContentProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movies";

        public static final String MOVIE_ID = "id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String POSTER = "poster";
        public static final String BACKDROP = "backdrop";
        public static final String OVERVIEW = "overview";
        public static final String RATING = "rating";
        public static final String RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
