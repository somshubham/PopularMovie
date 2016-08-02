package com.movie.som.popularmovie;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MovieAdapter extends BaseAdapter {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final ThreadLocal<Context> context;
    private final List<String> urls = new ArrayList<String>();

    public MovieAdapter(final Context context) {
        this.context = new ThreadLocal<Context>() {
            @Override
            protected Context initialValue() {
                return context;
            }
        };
        Collections.addAll(urls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ImageView(context.get());
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);;


        String url = getItem(position);

        Log.e(LOG_TAG," URL "+url);

        Picasso.with(context.get()).load(url).into(imageView);

        return convertView;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    public void replace(List<String> urls) {
        this.urls.clear();
        this.urls.addAll(urls);

    }
}
