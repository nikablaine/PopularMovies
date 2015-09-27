package org.nanodegree.android.krafla.popularmovies.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static org.nanodegree.android.krafla.popularmovies.data.Constants.*;

/**
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class Review implements Parcelable {

    public static final String ID_KEY = "id";
    public static final String AUTHOR_KEY = "author";
    public static final String CONTENT_KEY = "content";

    /**
     * Id of a review.
     */
    private String id;

    /**
     * Author of a review.
     */
    private String author;

    /**
     * Review itself.
     */
    private String content;

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public static Review parseFromJSONObject(JSONObject reviewObject) throws JSONException {
        String id = reviewObject.getString(ID_KEY);
        String author = reviewObject.getString(AUTHOR_KEY);
        String content = reviewObject.getString(CONTENT_KEY);
        return new Review(id, author, content);
    }

    /**
     * Creates a bundle with all the information about a review.
     *
     * @return bundle with a review information
     */
    public Bundle assembleBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ID_KEY, id);
        bundle.putString(AUTHOR_KEY, author);
        bundle.putString(CONTENT_KEY, content);
        return bundle;
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    private Review(Parcel in) {
        Bundle bundle = in.readBundle();
        this.id = bundle.getString(ID_KEY);
        this.author = bundle.getString(AUTHOR_KEY);
        this.content = bundle.getString(CONTENT_KEY);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(ID_KEY + SEPARATOR + id + END_OF_LINE)
                .append(AUTHOR_KEY + SEPARATOR + author + END_OF_LINE)
                .append(CONTENT_KEY + SEPARATOR + content)
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

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
