package org.nanodegree.android.krafla.popularmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility class that contains methods for quick parsing of JSON string returned by movieDB.org
 */
public class JsonUtil {

    public static ArrayList<Movie> parseMovieString(String jsonString) throws JSONException {
        JSONArray movieArray = getResultsArrayFromJson(jsonString);
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieObject = (JSONObject) movieArray.get(i);
            movies.add(Movie.parseFromJSONObject(movieObject));

        }
        return movies;
    }

    public static ArrayList<Trailer> parseTrailerString(String jsonString) throws JSONException {
        JSONArray trailersArray = getResultsArrayFromJson(jsonString);
        ArrayList<Trailer> trailerList = new ArrayList<>();
        for (int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailerObject = (JSONObject) trailersArray.get(i);
            trailerList.add(Trailer.parseFromJSONObject(trailerObject));

        }
        return trailerList;
    }

    public static ArrayList<Review> parseReviewString(String jsonString) throws JSONException {
        JSONArray reviewArray = getResultsArrayFromJson(jsonString);
        ArrayList<Review> reviewsList = new ArrayList<>();
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject reviewObject = (JSONObject) reviewArray.get(i);
            reviewsList.add(Review.parseFromJSONObject(reviewObject));

        }
        return reviewsList;
    }

    private static JSONArray getResultsArrayFromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getJSONArray("results");
    }
}
