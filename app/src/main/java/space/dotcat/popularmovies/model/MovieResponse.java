package space.dotcat.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {

    @SerializedName("results")
    private List<Movie> mMovieList;

    public MovieResponse(List<Movie> movieList) {
        mMovieList = movieList;
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(List<Movie> movieList) {
        mMovieList = movieList;
    }
}
