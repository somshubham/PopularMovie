package com.movie.som.popularmovie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
public class MovieAdapter extends BaseAdapter {

    private Context mContext;

    private String[] mThumbIds;

    public MovieAdapter(Context c, String[] str2) {

        mContext = c;
        mThumbIds = str2;
    }

    @Override
    public int getCount() {
        if (mThumbIds != null) {
            return mThumbIds.length;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
       // return null;
    }

    @Override
    public long getItemId(int position) {

        return position;

      //  return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(550,550));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(mThumbIds[position]).into(imageView);
        return imageView;
    }
}
