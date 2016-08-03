package com.movie.som.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetailFragment extends Fragment {


    private String data;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        Intent intent = getActivity().getIntent();

            data = intent.getStringExtra("title");
            TextView t=(TextView)rootView.findViewById(R.id.Title);
            t.setText(data);
            data = intent.getStringExtra("overview");
            t=(TextView)rootView.findViewById(R.id.overview);
            t.setText(data);
            data = intent.getStringExtra("vote_average");
            t=(TextView)rootView.findViewById(R.id.User_Rating);
            t.setText(data);
            data = intent.getStringExtra("release_date");
            t=(TextView)rootView.findViewById(R.id.Release_Date);
            t.setText(data);

           data = intent.getStringExtra("url");
        ImageView imageView=(ImageView)rootView.findViewById(R.id.movie_poster);
        Picasso.with(getActivity()).load(data).into(imageView);
        /*  Uri url= Uri.parse(data);
        Picasso.load(url).into(imageView);*/
        return rootView;





    }
}
