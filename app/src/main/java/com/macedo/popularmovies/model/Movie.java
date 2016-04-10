package com.macedo.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bia_m on 4/9/2016.
 */
public class Movie implements Parcelable {
    String poster;
    Boolean adult;
    String overview;
    String releaseDate;
    int[] genres;
    int id;
    String originalTitle;
    String originalLanguage;
    String title;
    Double popularity;
    int voteCount;
    Double voteAvg;

    public Movie(String poster, Boolean adult, String overview, String releaseDate,
                 int[] genres, int id, String originalTitle, String originalLanguage,
                 String title, Double popularity, int voteCount, Double voteAvg) {
        this.poster = poster;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAvg = voteAvg;
    }

    protected Movie(Parcel in) {
        poster = in.readString();
        adult = in.readInt() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        genres = in.createIntArray();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        voteAvg = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(poster);
        dest.writeInt(adult ? 1 : 0);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeIntArray(genres);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAvg);

    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Double getVoteAvg() {
        return voteAvg;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
}
