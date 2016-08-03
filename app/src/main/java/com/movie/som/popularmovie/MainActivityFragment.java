package com.movie.som.popularmovie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    MovieAdapter mAdapter;
    static String[] string1;
    GridView gridView;
    String[] urls,ids,overviews,vote_averages,release_dates,titles;
    public MainActivityFragment() {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);
    }

    private void updateMovie() {
        FetchMovieData movieTask = new FetchMovieData();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String m = prefs.getString(getString(R.string.pref_movie),getString(R.string.pref_popular));
        movieTask.execute(m);
    }
    public void onStart() {
        super.onStart();
        updateMovie();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id == R.id.refresh)
        {
            FetchMovieData movieData = new FetchMovieData();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String m = prefs.getString(getString(R.string.pref_movie),getString(R.string.pref_popular));
            movieData.execute(m);
            Log.v("myin","Refreshing the movies");

            return true;
        }
        if (id == R.id.action_settings)
        {
            Intent intent=new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView =  (GridView) rootView.findViewById(R.id.gridView);
        String[] strings;


        //updating the movie for the first time ..........
        updateMovie();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



         int m= (int) mAdapter.getItem(position);

                String k=titles[m];
                Toast.makeText(getActivity(), ""+k, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),MovieDetail.class);
                intent.putExtra("title",""+titles[m]);
                intent.putExtra("overview",""+overviews[m]);
                intent.putExtra("url",""+urls[m]);
                intent.putExtra("vote_average",""+vote_averages[m]);
                intent.putExtra("release_date",""+release_dates[m]);
                 startActivity(intent);



            }
        });




        return rootView;

    }





    public class FetchMovieData extends AsyncTask<String, Void,String[] > {

        private final String LOG_TAG = FetchMovieData.class.getSimpleName();


        private String[] getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String poster_path = "poster_path";
            final String id = "id";
            final String adult = "adult";
            final String overview = "overview";
            final String release_date = "release_date";
            final String popularity = "popularity";
            final String title = "title";
            final String vote_average="vote_average";


            JSONObject MovieJson = new JSONObject(MovieJsonStr);
            JSONArray MovieArray = MovieJson.getJSONArray(OWM_LIST);
            int k=MovieArray.length();

               urls = new String[k];
               overviews=new String[k];
               vote_averages=new String[k];
               release_dates=new String[k];
               titles=new String[k];

            for(int i = 0; i < MovieArray.length(); i++) {


                // Get the JSON object representing the movie
                JSONObject moviearraydata = MovieArray.getJSONObject(i);
                int int_variable;

                String idvalue = moviearraydata.getString(poster_path);
                urls[i]=("http://image.tmdb.org/t/p/w185" +idvalue);
                idvalue = moviearraydata.getString(overview);
                overviews[i]=""+idvalue;
                int_variable = moviearraydata.getInt(vote_average);
                vote_averages[i]=""+int_variable;
                idvalue = moviearraydata.getString(release_date);
                release_dates[i]=""+idvalue;
                idvalue = moviearraydata.getString(title);
                titles[i]=""+idvalue;






            }


            for (String s : urls) {
                Log.v(LOG_TAG, "Movie id: " + s);
            }
            return urls;

        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }



            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String MovieJsonStr = null;

            String format = "json";




            try {


                final String Movie_BASE_URL2 =
                        "https://api.themoviedb.org/3/movie/"+params[0]+"?api_key="+BuildConfig.Movie_db_key;




                URL url = new URL(Movie_BASE_URL2);

                Log.v(LOG_TAG, "Built URI " + Movie_BASE_URL2);

                // Create the request to themoviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                MovieJsonStr = buffer.toString();



                Log.v("Movie",""+MovieJsonStr);
                Log.v(LOG_TAG, "Movie string: " + MovieJsonStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(MovieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }




            return null;
        }


        protected void onPostExecute(String[] strings) {

            mAdapter = new MovieAdapter(getActivity(),strings);
            gridView.setAdapter(mAdapter);




        }




    }





}


