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

    /**
     * Load popular movies via local or remote data source
     *
     * @return observable flowable which contains list of movies
     */

    Flowable<List<Movie>> getPopularMovies();

    Flowable<List<Movie>> getPopularMoviesSortedByRating();

    Flowable<List<Movie>> getPopularMoviesSortedByPopularity();

    Flowable<List<Movie>> getFavoriteMovies();

    void deleteAllMoviesSync();

    void addMoviesSync(List<Movie> movies);

    Flowable<List<Movie>> reloadMovies();

    LiveData<Movie> getMovieById(int movieId);

    Single<Video> getTrailer(int movieId);

    Single<List<Review>> getReviews(int movieId);

    Single<MovieExtraInfo> getTrailersAndReviews(int movieId);

    void addTrailerSync(Video ... videos);

    void addReviewsSync(List<Review> reviews);

    Completable updateMovie(Movie movie);
}
