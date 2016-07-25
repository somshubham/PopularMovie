package com.movie.som.popularmovie;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by som on 25/07/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();


    public MovieAdapter(Activity context, List<Movie>resource) {
        super(context,0, resource);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_text, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.imageView);
        iconView.setImageResource(movie.image);

        TextView versionNameView = (TextView) convertView.findViewById(R.id.textView);
        versionNameView.setText(movie.versionName
                + " - " + movie.versionNumber );

        return convertView;
    }
}
