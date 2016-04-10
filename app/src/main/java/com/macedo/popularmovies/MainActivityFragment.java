package com.macedo.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.macedo.popularmovies.task.FetchMoviesTask;

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
        FetchMoviesTask moviesTask = new FetchMoviesTask(progress, mMovieAdapter);
        moviesTask.execute(sortingOption);
    }

}
