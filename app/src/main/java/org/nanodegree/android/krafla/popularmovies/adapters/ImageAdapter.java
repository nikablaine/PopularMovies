package org.nanodegree.android.krafla.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.nanodegree.android.krafla.popularmovies.R;
import org.nanodegree.android.krafla.popularmovies.data.Constants;
import org.nanodegree.android.krafla.popularmovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Special adapter needed to display posters in a grid view.
 *
 * @author Veronika Rodionova nika.blaine@gmail.com
 */
public class ImageAdapter extends BaseAdapter {

    private final Context context;
    private final Object lock = new Object();
    private List<Movie> movieCollection;

    public ImageAdapter(Context context) {
        this.context = context;
        this.movieCollection = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return movieCollection.size();
    }

    @Override
    public Object getItem(int position) {
        return movieCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = movieCollection.get(position);

        if (movie == null) {
            return null;
        }

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
        }

        Picasso.with(context).load(movie.getUriForPoster(Constants.DEFAULT_POSTER_SIZE)).into((ImageView) convertView);

        return convertView;
    }

    public void clear() {
        synchronized (lock) {
            movieCollection = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void updateData(List<Movie> movieCollection) {
        synchronized (lock) {
            clear();
            this.movieCollection = movieCollection;
        }
        notifyDataSetChanged();
    }
}
