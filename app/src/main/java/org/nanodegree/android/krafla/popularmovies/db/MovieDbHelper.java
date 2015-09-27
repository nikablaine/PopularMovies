package org.nanodegree.android.krafla.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.POSTER + " TEXT, " +
                MovieContract.MovieEntry.BACKDROP + " TEXT, " +
                MovieContract.MovieEntry.OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.RATING + " DOUBLE, " +
                MovieContract.MovieEntry.RELEASE_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
