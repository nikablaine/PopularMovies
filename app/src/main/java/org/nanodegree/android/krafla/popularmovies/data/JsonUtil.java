package org.nanodegree.android.krafla.popularmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that contains methods for quick parsing of JSON string returned by movieDB.org
 */
public class JsonUtil {

    public static List<Movie> parseMovieString(String jsonString) throws JSONException {

        List<Movie> movies = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray movieArray = jsonObject.getJSONArray("results");
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieObject = (JSONObject) movieArray.get(i);
            movies.add(Movie.parseFromJSONObject(movieObject));

        }
        return movies;
    }
}
