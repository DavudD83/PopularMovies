package space.dotcat.popularmovies.repository.moviesRepository;

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
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.LocalMoviesSource;
import space.dotcat.popularmovies.repository.moviesRepository.remoteMoviesSource.RemoteMoviesSource;

public class MoviesRepositoryImpl implements MoviesRepository {

    private LocalMoviesSource mLocalDataSource;

    private RemoteMoviesSource mRemoteDataSource;

    @Inject
    public MoviesRepositoryImpl(LocalMoviesSource localDataSource,
                                RemoteMoviesSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }


    @Override
    public Flowable<List<Movie>> getMoviesWithFlag(String flag) {
        return mLocalDataSource.getMoviesByFlag(flag);
    }

    @Override
    public Flowable<List<Movie>> getMoviesWithFlagSortedByRating(String flag) {
        return mLocalDataSource.getMoviesWithFlagSortedByRating(flag);
    }

    @Override
    public Flowable<List<Movie>> getMoviesWithFlagSortedByPopularity(String flag) {
        return mLocalDataSource.getMoviesWithFlagSortedByPopularity(flag);
    }

    @Override
    public Flowable<List<Movie>> getMoviesWithFlagSortedByDate(String flag) {
        return mLocalDataSource.getMoviesWithFlagSortedByReleaseDate(flag);
    }

    @Override
    public Flowable<List<Movie>> reloadMoviesWithFlag(String flag) {
        return mRemoteDataSource.reloadMoviesWithFlag(flag)
                .doOnNext(movies -> {
                    if (movies.isEmpty()) {
                        return;
                    }

                    mLocalDataSource.updateReloadedMoviesSync(movies, flag);
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
    public Completable updateMovie(Movie movie) {
        return mLocalDataSource.updateMovie(movie);
    }

    @Override
    public int deleteMoviesWithoutFlags() {
        return mLocalDataSource.deleteMoviesWithoutFlags();
    }
}
