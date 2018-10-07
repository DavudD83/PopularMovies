package space.dotcat.popularmovies.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.ReviewResponse;
import space.dotcat.popularmovies.model.Video;
import space.dotcat.popularmovies.model.VideoResponse;

public interface MoviesRepository {

    Flowable<List<Movie>> getMoviesWithFlag(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByRating(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByPopularity(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByDate(String flag);

    Flowable<List<Movie>> reloadMoviesWithFlag(String flag);

    LiveData<Movie> getMovieById(int movieId);

    Single<Video> getTrailer(int movieId);

    Single<List<Review>> getReviews(int movieId);

    Single<MovieExtraInfo> getTrailersAndReviews(int movieId);

    Completable updateMovie(Movie movie);
}
