package space.dotcat.popularmovies.repository.remoteMoviesSource;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import space.dotcat.popularmovies.api.ApiService;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.MovieResponse;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.ReviewResponse;
import space.dotcat.popularmovies.model.Video;
import space.dotcat.popularmovies.model.VideoResponse;
import space.dotcat.popularmovies.repository.MoviesRepository;

public class RemoteMoviesSourceImpl implements MoviesRepository {

    public final static String TRAILER_VIDEO_TYPE = "Trailer";

    private final ApiService mApiService;

    public RemoteMoviesSourceImpl(ApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Flowable<List<Movie>> getPopularMovies() {
        return mApiService.getPopularMovies()
                .toFlowable()
                .map(MovieResponse::getMovieList);
    }

    @Override
    public Flowable<List<Movie>> getPopularMoviesSortedByRating() {
        //is not supported operation
        return null;
    }

    @Override
    public Flowable<List<Movie>> getPopularMoviesSortedByPopularity() {
        //is not supported operation
        return null;
    }

    @Override
    public Flowable<List<Movie>> getFavoriteMovies() {
        //is not supported operation
        return null;
    }

    @Override
    public void deleteAllMoviesSync() {
        //is not supported operation
    }

    @Override
    public void addMoviesSync(List<Movie> movies) {
        //is not supported operation
    }

    @Override
    public Flowable<List<Movie>> reloadMovies() {
        //is not supported operation
        return null;
    }

    @Override
    public LiveData<Movie> getMovieById(int movieId) {
        //is not supported operation
        return null;
    }

    @Override
    public Single<Video> getTrailer(int movieId) {
        return mApiService.getVideos(movieId)
                .toObservable()
                .map(VideoResponse::getVideos)
                .flatMap(Observable::fromIterable)
                .filter(video -> video.getType().equals(TRAILER_VIDEO_TYPE))
                .firstOrError();
    }

    @Override
    public Single<List<Review>> getReviews(int movieId) {
        return mApiService.getReviews(movieId)
                .map(ReviewResponse::getReviews);
    }

    @Override
    public Single<MovieExtraInfo> getTrailersAndReviews(int movieId) {
        return Single.zip(getTrailer(movieId), getReviews(movieId), MovieExtraInfo::new);
    }

    @Override
    public void addTrailerSync(Video... videos) {
        //is not supported operation
    }

    @Override
    public void addReviewsSync(List<Review> reviews) {
        //is not supported operation
    }

    @Override
    public Completable updateMovie(Movie movie) {
        //is not supported operation
        return null;
    }
}
