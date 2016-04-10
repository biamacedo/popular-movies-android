package com.macedo.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.macedo.popularmovies.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private final String IMAGE_SIZE = "w185/";

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Movie movie = getActivity().getIntent().getParcelableExtra(MainActivityFragment.MOVIE_EXTRA);

        ImageView posterView = (ImageView) rootView.findViewById(R.id.poster);
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        titleView.setText(movie.getOriginalTitle());
        TextView overviewView = (TextView) rootView.findViewById(R.id.overview);
        overviewView.setText(movie.getOverview());

        TextView releaseDateView = (TextView) rootView.findViewById(R.id.releaseDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = null;
        try {
            releaseDate = dateFormat.parse(movie.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (releaseDate == null){
                releaseDateView.setText("Date not found");
            } else {
                SimpleDateFormat showFormat = new SimpleDateFormat("MM/dd/yyyy");
                releaseDateView.setText(showFormat.format(releaseDate));
            }
        }

        TextView userRating = (TextView) rootView.findViewById(R.id.userRating);
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        userRating.setText(decimalFormat.format(movie.getVoteAvg()) + "/10");

//        ImageView star1View = (ImageView) rootView.findViewById(R.id.star1);
//        ImageView star2View = (ImageView) rootView.findViewById(R.id.star2);
//        ImageView star3View = (ImageView) rootView.findViewById(R.id.star3);
//        ImageView star4View = (ImageView) rootView.findViewById(R.id.star4);
//        ImageView star5View = (ImageView) rootView.findViewById(R.id.star5);
//
//        if (movie.getVoteAvg()/2 < 2){
//            star1View.setImageResource(R.drawable.ic_star);
//            star2View.setImageResource(R.drawable.ic_star_border);
//            star3View.setImageResource(R.drawable.ic_star_border);
//            star4View.setImageResource(R.drawable.ic_star_border);
//            star5View.setImageResource(R.drawable.ic_star_border);
//        } else if (movie.getVoteAvg()/2 < 3){
//            star1View.setImageResource(R.drawable.ic_star);
//            star2View.setImageResource(R.drawable.ic_star);
//            star3View.setImageResource(R.drawable.ic_star_border);
//            star4View.setImageResource(R.drawable.ic_star_border);
//            star5View.setImageResource(R.drawable.ic_star_border);
//        } else if (movie.getVoteAvg()/2 < 4){
//            star1View.setImageResource(R.drawable.ic_star);
//            star2View.setImageResource(R.drawable.ic_star);
//            star3View.setImageResource(R.drawable.ic_star);
//            star4View.setImageResource(R.drawable.ic_star_border);
//            star5View.setImageResource(R.drawable.ic_star_border);
//        } else if (movie.getVoteAvg()/2 < 5){
//            star1View.setImageResource(R.drawable.ic_star);
//            star2View.setImageResource(R.drawable.ic_star);
//            star3View.setImageResource(R.drawable.ic_star);
//            star4View.setImageResource(R.drawable.ic_star);
//            star5View.setImageResource(R.drawable.ic_star_border);
//        } else {
//            star1View.setImageResource(R.drawable.ic_star);
//            star2View.setImageResource(R.drawable.ic_star);
//            star3View.setImageResource(R.drawable.ic_star);
//            star4View.setImageResource(R.drawable.ic_star);
//            star5View.setImageResource(R.drawable.ic_star);
//        }

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.posterProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        Picasso
                .with(getContext())
                .load(new StringBuilder()
                        .append(IMAGE_URL)
                        .append(IMAGE_SIZE)
                        .append(movie.getPoster())
                        .toString())
                .into(posterView, new Callback() {
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


        return rootView;
    }
}
