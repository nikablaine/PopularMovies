package org.nanodegree.android.krafla.popularmovies.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing trailer of a movie.
 *
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class Trailer implements Parcelable {

    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String SITE_KEY = "site";
    public static final String KEY_KEY = "key";

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

    /**
     * Creates a bundle with all the information about a trailer.
     *
     * @return bundle with trailer information
     */
    public Bundle assembleBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ID_KEY, id);
        bundle.putString(NAME_KEY, name);
        bundle.putString(SITE_KEY, site);
        bundle.putString(KEY_KEY, key);
        return bundle;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    private Trailer(Parcel in) {
        Bundle bundle = in.readBundle();
        this.id = bundle.getString(ID_KEY);
        this.name = bundle.getString(NAME_KEY);
        this.site = bundle.getString(SITE_KEY);
        this.key = bundle.getString(KEY_KEY);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(ID_KEY + Constants.SEPARATOR + id + Constants.END_OF_LINE)
                .append(NAME_KEY + Constants.SEPARATOR + name + Constants.END_OF_LINE)
                .append(SITE_KEY + Constants.SEPARATOR + site)
                .append(KEY_KEY + Constants.SEPARATOR + key)
                .toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(assembleBundle());
    }
}
