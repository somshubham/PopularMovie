package com.movie.som.popularmovie;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    private MovieAdapter movieAdapter;

    Movie[] androidFlavors = {
            new Movie("Cupcake", "1.5", R.drawable.cupcake),
            new Movie("Donut", "1.6", R.drawable.donut),
            new Movie("Eclair", "2.0-2.1", R.drawable.eclair),

    };

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String [] forecast={"Movie1","Movie2","Movie3","Movie4"};


        List<String> array =new ArrayList<String>(Arrays.asList(forecast));
        ArrayAdapter<String> m=new ArrayAdapter<String>(getActivity(),R.layout.listview_text,R.id.textView,array);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView =  (ListView) rootView.findViewById(R.id.listView);

       movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(androidFlavors));

        listView.setAdapter(movieAdapter);

        return rootView;



    }
}
