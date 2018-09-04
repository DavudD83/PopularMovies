package space.dotcat.popularmovies.repository;

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

public class MoviesRepositoryImpl implements MoviesRepository {

    private MoviesRepository mLocalDataSource;

    private MoviesRepository mRemoteDataSource;

    @Inject
    public MoviesRepositoryImpl(MoviesRepository localDataSource,
                                MoviesRepository remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Flowable<List<Movie>> getPopularMovies() {
        return mLocalDataSource.getPopularMovies();
    }

    @Override
    public Flowable<List<Movie>> getPopularMoviesSortedByRating() {
        return mLocalDataSource.getPopularMoviesSortedByRating();
    }

    @Override
    public Flowable<List<Movie>> getPopularMoviesSortedByPopularity() {
        return mLocalDataSource.getPopularMoviesSortedByPopularity();
    }

    @Override
    public Flowable<List<Movie>> getFavoriteMovies() {
        return mLocalDataSource.getFavoriteMovies();
    }

    @Override
    public void deleteAllMoviesSync() {
        mLocalDataSource.deleteAllMoviesSync();
    }

    @Override
    public void addMoviesSync(List<Movie> movies) {
        mLocalDataSource.addMoviesSync(movies);
    }

    @Override
    public Flowable<List<Movie>> reloadMovies() {
        return mRemoteDataSource.getPopularMovies()
                .doOnNext(movies -> {
                    if(movies.isEmpty())
                        return;

                    mLocalDataSource.deleteAllMoviesSync();

                    mLocalDataSource.addMoviesSync(movies);
                });
    }

    @Override
    public LiveData<Movie> getMovieById(int movieId) {
        return mLocalDataSource.getMovieById(movieId);
    }

    @Override
    public Single<Video> getTrailer(int movieId) {
        return mLocalDataSource.getTrailer(movieId);
    }

    @Override
    public Single<List<Review>> getReviews(int movieId) {
        return mLocalDataSource.getReviews(movieId);
    }

    @Override
    public Single<MovieExtraInfo> getTrailersAndReviews(int movieId) {
            return mLocalDataSource.getTrailersAndReviews(movieId)
                .onErrorResumeNext(throwable -> mRemoteDataSource.getTrailersAndReviews(movieId)
                        .doOnSuccess(movieExtraInfo -> {
            mLocalDataSource.addTrailerSync(movieExtraInfo.getTrailer());

            mLocalDataSource.addReviewsSync(movieExtraInfo.getReviewList());
        }));
    }

    @Override
    public void addTrailerSync(Video... videos) {
        mLocalDataSource.addTrailerSync(videos);
    }

    @Override
    public void addReviewsSync(List<Review> reviews) {
        mLocalDataSource.addReviewsSync(reviews);
    }

    @Override
    public Completable updateMovie(Movie movie) {
        return mLocalDataSource.updateMovie(movie);
    }
}
