package com.macedo.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.macedo.popularmovies.R;
import com.macedo.popularmovies.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bia_m on 4/8/2016.
 */
public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mMovies;

    private final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private final String IMAGE_SIZE = "w185/";

    public MovieAdapter(Context mContext, List<Movie> images) {
        this.mContext = mContext;
        this.mMovies = images;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void add(Movie movie){
        mMovies.add(movie);
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movies){
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie item = mMovies.get(position);

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Picasso
                .with(mContext)
                .load(new StringBuilder()
                            .append(IMAGE_URL)
                            .append(IMAGE_SIZE)
                            .append(item.getPoster())
                            .toString())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        // TODO: Error Loading Poster, Solution: Load Placeholder!
                    }
                });


        return convertView;
    }
}
