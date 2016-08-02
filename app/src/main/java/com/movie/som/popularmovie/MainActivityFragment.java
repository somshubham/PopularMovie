package com.movie.som.popularmovie;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


     MovieAdapter movieAdapter;


    private ArrayAdapter<String> mForecastAdapter;

    Movie[] androidFlavors = {
            new Movie("Cupcake", "1.5", R.drawable.cupcake),
            new Movie("Donut", "1.6", R.drawable.donut),
            new Movie("Eclair", "2.0-2.1", R.drawable.eclair),

    };

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id == R.id.refresh)
        {
            FetchMovieData movieData = new FetchMovieData();
            movieData.execute("94043");
            Log.v("myin","myyyyyyyyyy");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        String[] data = {
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };



        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));


       // mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_text, R.id.textView, weekForecast);
        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_text,R.id.textView,weekForecast);
        //////-------------------------

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView GridView =  (GridView) rootView.findViewById(R.id.gridView);

     // movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(androidFlavors));
        movieAdapter=new MovieAdapter(getActivity());
        //GridView.setAdapter(movieAdapter);
        GridView.setAdapter(movieAdapter);
        updateMovie();
        // Picasso.with(this).load("YOUR IMAGE URL HERE").into(imageView);





        return rootView;

    }


    private void updateMovie() {
        FetchMovieData movieTask = new FetchMovieData();
        movieTask.execute();
    }


    public class FetchMovieData extends AsyncTask<String, Void,List<String>> {

        private final String LOG_TAG = FetchMovieData.class.getSimpleName();


        private List<String> getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String id = "poster_path";


            JSONObject MovieJson = new JSONObject(MovieJsonStr);
            JSONArray MovieArray = MovieJson.getJSONArray(OWM_LIST);
            int k=MovieArray.length();

              String[] resultStrs=new String[k] ;
            List<String> urls = new ArrayList<>();

            for(int i = 0; i < MovieArray.length(); i++) {


                // Get the JSON object representing the movie
                JSONObject moviearraydata = MovieArray.getJSONObject(i);

              // int idvalue = moviearraydata.getInt(id);
             String idvalue = moviearraydata.getString(id);


                //resultStrs[i] ="http://image.tmdb.org/t/p/w185"+idvalue;
                urls.add("http://image.tmdb.org/t/p/w185" +idvalue);
            }


            for (String s : urls) {
                Log.v(LOG_TAG, "Movie id: " + s);

//mForecastAdapter.add(s);


            }
            return urls;

        }

        @Override
        protected List<String> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            // If there's no zip code, there's nothing to look up.  Verify size of params.
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
                        "https://api.themoviedb.org/3/movie/popular?api_key="+BuildConfig.Movie_db_key;




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
    }


    protected void onPostExecute(List<String> strings) {
     // MovieAdapter.replace(strings);
      //  MovieAdapter.replace(strings);

    }



}
