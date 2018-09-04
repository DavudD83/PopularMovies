package space.dotcat.popularmovies.repository.localMoviesSource;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;
import space.dotcat.popularmovies.repository.MoviesDao;
import space.dotcat.popularmovies.repository.MoviesRepository;

public class LocalMoviesSourceImpl implements MoviesRepository {

    private MoviesDao mMoviesDao;

    @Inject
    public LocalMoviesSourceImpl(MoviesDao moviesDao) {
        mMoviesDao = moviesDao;
    }

    @Override
    public Flowable<List<Movie>> getPopularMovies() {
        return mMoviesDao.getMovies();
    }

    @Override
    public Flowable<List<Movie>> getPopularMoviesSortedByRating() {
        return mMoviesDao.getMoviesSortedByRating();
    }

    @Override
    public Flowable<List<Movie>> getPopularMoviesSortedByPopularity() {
        return mMoviesDao.getMoviesSortedByPopularity();
    }

    @Override
    public Flowable<List<Movie>> getFavoriteMovies() {
        return mMoviesDao.getFavoriteMovies();
    }

    @Override
    public void deleteAllMoviesSync() {
        mMoviesDao.deleteAllMovies();
    }

    @Override
    public void addMoviesSync(List<Movie> movies) {
        mMoviesDao.insertMovies(movies);
    }

    @Override
    public Flowable<List<Movie>> reloadMovies() {
        //is not supported operation
        return null;
    }

    @Override
    public LiveData<Movie> getMovieById(int movieId) {
        return mMoviesDao.getMovieById(movieId);
    }

    @Override
    public Single<Video> getTrailer(int movieId) {
        return mMoviesDao.getTrailer(movieId);
    }

    @Override
    public Single<List<Review>> getReviews(int movieId) {
        return mMoviesDao.getReviews(movieId);
    }

    @Override
    public Single<MovieExtraInfo> getTrailersAndReviews(int movieId) {
        return mMoviesDao.getMovieExtraInfo(movieId);
    }

    @Override
    public void addTrailerSync(Video... videos) {
        mMoviesDao.insertTrailer(videos);
    }

    @Override
    public void addReviewsSync(List<Review> reviews) {
        mMoviesDao.insertReviews(reviews);
    }

    @Override
    public Completable updateMovie(Movie movie) {
        return Completable.fromAction(() -> mMoviesDao.updateMovie(movie));
    }
}
