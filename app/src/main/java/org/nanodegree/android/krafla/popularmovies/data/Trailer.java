package org.nanodegree.android.krafla.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing trailer of a movie.
 *
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class Trailer {

    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String SITE_KEY = "site";
    public static final String KEY_KEY = "key";
    private static final String SEPARATOR = ": ";
    private static final String END_OF_LINE = "; ";

    /**
     * Id of a trailer ine the movie database.
     */
    private String id;

    /**
     * Name of a trailer.
     */
    private String name;

    /**
     * Site of a trailer.
     */
    private String site;

    /**
     * YouTube key of a trailer.
     */
    private String key;

    public Trailer(String id, String name, String site, String key) {
        this.id = id;
        this.name = name;
        this.site = site;
        this.key = key;
    }

    public static Trailer parseFromJSONObject(JSONObject trailerObject) throws JSONException {
        String id = trailerObject.getString(ID_KEY);
        String name = trailerObject.getString(NAME_KEY);
        String site = trailerObject.getString(SITE_KEY);
        String key = trailerObject.getString(KEY_KEY);
        return new Trailer(id, name, site, key);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(ID_KEY + SEPARATOR + id + END_OF_LINE)
                .append(NAME_KEY + SEPARATOR + name + END_OF_LINE)
                .append(SITE_KEY + SEPARATOR + site)
                .append(KEY_KEY + SEPARATOR + key)
                .toString();
    }
}
