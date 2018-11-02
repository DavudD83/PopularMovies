package space.dotcat.popularmovies.repository.moviesRepository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;

public interface MoviesRepository {

    Flowable<List<Movie>> getMoviesWithFlag(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByRating(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByPopularity(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByDate(String flag);

    Flowable<List<Movie>> reloadMoviesWithFlag(String flag);

    LiveData<Movie> getMovieById(int movieId);

    Single<List<Video>> getTrailer(int movieId);

    Single<List<Review>> getReviews(int movieId);

    Flowable<MovieExtraInfo> getTrailersAndReviews(int movieId);

    Completable updateMovie(Movie movie);

    int deleteMoviesWithoutFlags();
}
