package org.nanodegree.android.krafla.popularmovies.data;

public class MovieBuilder {
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private double userRating;
    private String releaseDate;
    private String id;

    public MovieBuilder() {
    }

    public MovieBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public MovieBuilder setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public MovieBuilder setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public MovieBuilder setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public MovieBuilder setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public MovieBuilder setUserRating(double userRating) {
        this.userRating = userRating;
        return this;
    }

    public MovieBuilder setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Movie createMovie() {
        return new Movie(id, originalTitle, posterPath, backdropPath, overview, userRating, releaseDate);
    }
}