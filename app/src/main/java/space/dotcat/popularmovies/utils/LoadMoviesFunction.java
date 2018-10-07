package space.dotcat.popularmovies.utils;

import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.popularmovies.model.Movie;

public interface LoadMoviesFunction {

    Flowable<List<Movie>> loadMovies();
}
