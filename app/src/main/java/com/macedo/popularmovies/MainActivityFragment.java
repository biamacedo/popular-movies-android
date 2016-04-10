package com.macedo.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.macedo.popularmovies.adapter.MovieAdapter;
import com.macedo.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private List<Movie> mMovies = new ArrayList<Movie>();
    private MovieAdapter mMovieAdapter;
    private ProgressDialog progress;

    public static final String MOVIE_EXTRA = "MOVIE_EXTRA";
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        progress = new ProgressDialog(getContext());
//        progress.setTitle("Loading");
        progress.setMessage("Wait while loading movies...");
        progress.setCancelable(false);
        setHasOptionsMenu(true);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Integer sortingOption = settings.getInt(getString(R.string.pref_sorting_key), Integer.parseInt(getString(R.string.pref_sorting_default)));
        Log.d(LOG_TAG, "Sorting Option: " + sortingOption);
        updateMovies(sortingOption);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new MovieAdapter(getActivity(), mMovies);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(MOVIE_EXTRA, mMovieAdapter.getItem(position));
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Integer sortingOption = settings.getInt(getString(R.string.pref_sorting_key), Integer.parseInt(getString(R.string.pref_sorting_default)));
        Log.d(LOG_TAG, "Sorting Option: " + sortingOption);
        switch (id){
            case R.id.action_refresh:
                updateMovies(sortingOption);
                return true;
            case R.id.action_sort_popular:
                if (sortingOption != 0){
                    sortingOption = 0;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(getString(R.string.pref_sorting_key), sortingOption);
                    editor.commit();
                    updateMovies(sortingOption);
                }
                return true;
            case R.id.action_sort_rating:
                if (sortingOption != 1){
                    sortingOption = 1;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(getString(R.string.pref_sorting_key), sortingOption);
                    editor.commit();
                    updateMovies(sortingOption);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(Integer sortingOption){
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(sortingOption);
    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(Integer... params) {
            Log.d(LOG_TAG, "Starting Task");

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String POPULAR_MOVIES = "/movie/popular";
                final String TOP_RATED_MOVIES = "/movie/top_rated";
                final String API_KEY = "api_key";

                String sortingMethod = "";
                Integer sortingOption = params[0];
                Log.d(LOG_TAG, "Sorting Option: " + sortingOption);
                switch (sortingOption){
                    case 1:
                        sortingMethod = TOP_RATED_MOVIES;
                        break;
                    default:
                        sortingMethod = POPULAR_MOVIES;
                        break;
                }

                Uri buildUri = Uri.parse("http://api.themoviedb.org/3" + sortingMethod + "?").buildUpon()
                        .appendQueryParameter(API_KEY, "<API_KEY>")
                        .build();

                Log.d(LOG_TAG, buildUri.toString());

                String moviesJson = null;

                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null){
                    Log.e(LOG_TAG, "InputStream Null");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0){
                    Log.e(LOG_TAG, "Stream was empty");
                    return null;
                }

                moviesJson = buffer.toString();
                Log.d(LOG_TAG, "Movies JSON String:" + moviesJson);

                try {
                    return getMoviesDataFromJson(moviesJson);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error Parsing Object from API:", e);
//                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                Log.d(LOG_TAG, "Error" + e.getMessage());
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.d(LOG_TAG, "Error" + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(LOG_TAG, "Error" + e.getMessage());
                e.printStackTrace();
            } finally {
                Log.d(LOG_TAG, "Finishing Task");

                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.show();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            progress.dismiss();

            if (movies != null){
                mMovieAdapter.clear();
                mMovieAdapter.addAll(movies);
            }
        }

        private List<Movie> getMoviesDataFromJson(String moviesDataStr) throws JSONException {

            final String RESULTS = "results";

            final String POSTER = "poster_path";
            final String ADULT = "adult";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String GENRES = "genre_ids";
            final String ID = "id";
            final String ORIGINAL_TITLE = "original_title";
            final String ORIGINAL_LANGUAGE = "original_language";
            final String TITLE = "title";
            final String POPULARITY = "popularity";
            final String VOTE_COUNT = "vote_count";
            final String VOTE_AVG = "vote_average";

            JSONObject moviesJson = new JSONObject(moviesDataStr);
            JSONArray moviesResult = moviesJson.getJSONArray(RESULTS);

            List<Movie> movies = new ArrayList<>();

            for (int i = 0; i < moviesResult.length(); i++){
                JSONObject movieObj = (JSONObject) moviesResult.get(i);

                JSONArray genresArray = movieObj.getJSONArray(GENRES);
                int[] genres = new int[genresArray.length()];
                for(int g = 0; i<genresArray.length(); i++){
                    genres[i] = genresArray.getInt(i);
                }

                Movie newMovie = new Movie(
                        movieObj.getString(POSTER),
                        movieObj.getBoolean(ADULT),
                        movieObj.getString(OVERVIEW),
                        movieObj.getString(RELEASE_DATE),
                        genres,
                        movieObj.getInt(ID),
                        movieObj.getString(ORIGINAL_TITLE),
                        movieObj.getString(ORIGINAL_LANGUAGE),
                        movieObj.getString(TITLE),
                        movieObj.getDouble(POPULARITY),
                        movieObj.getInt(VOTE_COUNT),
                        movieObj.getDouble(VOTE_AVG)
                );
                movies.add(newMovie);
            }


            return movies;
        }
    }
}
