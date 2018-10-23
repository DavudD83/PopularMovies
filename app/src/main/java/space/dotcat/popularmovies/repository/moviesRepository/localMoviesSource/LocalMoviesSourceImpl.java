package space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource;

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

public class LocalMoviesSourceImpl implements LocalMoviesSource {

    private MoviesDao mMoviesDao;

    @Inject
    public LocalMoviesSourceImpl(MoviesDao moviesDao) {
        mMoviesDao = moviesDao;
    }

    @Override
    public Flowable<List<Movie>> getMoviesByFlag(String flag) {
        return mMoviesDao.getMoviesByFlag(flag);
    }

    @Override
    public Flowable<List<Movie>> getMoviesWithFlagSortedByRating(String flag) {
        return mMoviesDao.getMoviesWithFlagSortedByRating(flag);
    }

    @Override
    public Flowable<List<Movie>> getMoviesWithFlagSortedByPopularity(String flag) {
        return mMoviesDao.getMoviesWithFlagSortedByPopularity(flag);
    }

    @Override
    public Flowable<List<Movie>> getMoviesWithFlagSortedByReleaseDate(String flag) {
        return mMoviesDao.getMoviesWithFlagSortedByDate(flag);
    }

    @Override
    public void addMoviesSync(List<Movie> movies) {
        mMoviesDao.insertMovies(movies);
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

    @Override
    public void updateReloadedMoviesSync(List<Movie> movies, String flag) {
        mMoviesDao.updateReloadedMovies(movies, flag);
    }

    @Override
    public void deleteAllMovies() {
        mMoviesDao.deleteAllMovies();
    }

    @Override
    public int deleteMoviesWithoutFlags() {
        return mMoviesDao.deleteMoviesWithoutFlags();
    }
}