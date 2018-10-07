package space.dotcat.popularmovies.repository.remoteMoviesSource;

import android.support.annotation.VisibleForTesting;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import space.dotcat.popularmovies.utils.LoadMoviesFunction;
import space.dotcat.popularmovies.utils.date.DateProvider;

public class RemoteMoviesSourceImpl implements RemoteMoviesSource {

    public static final String TRAILER_VIDEO_TYPE = "Trailer";

    private static final String PRIMARY_RELEASE_DAY_GTE = "primary_release_date.gte";

    private static final String PRIMARY_RELEASE_DAY_LTE = "primary_release_date.lte";

    private final ApiService mApiService;

    private final DateProvider mDateProvider;

    private HashMap<String, LoadMoviesFunction> mLoadMoviesFunctions;

    public RemoteMoviesSourceImpl(ApiService apiService, DateProvider dateProvider) {
        mApiService = apiService;

        mDateProvider = dateProvider;

        mLoadMoviesFunctions = new HashMap<>();

        mLoadMoviesFunctions.put(Movie.FLAG_POPULAR, this::reloadPopularMovies);
        mLoadMoviesFunctions.put(Movie.FLAG_ONGOING, this::reloadOngoingMovies);
        mLoadMoviesFunctions.put(Movie.FLAG_UPCOMING, this::reloadUpcomingMovies);
    }

    @VisibleForTesting
    public Flowable<List<Movie>> reloadPopularMovies() {
        return mApiService.getPopularMovies()
                .toFlowable()
                .map(MovieResponse::getMovieList)
                .flatMap(Flowable::fromIterable)
                .doOnNext(movie -> movie.setPopular(true))
                .toList()
                .toFlowable();
    }

    @VisibleForTesting
    public Flowable<List<Movie>> reloadOngoingMovies() {
        HashMap<String, String> complexQuery = new HashMap<>();

        String startDate = mDateProvider.getStartMovieDateForOngoing();
        String endDate = mDateProvider.getEndMovieDateForOngoing();

        complexQuery.put(PRIMARY_RELEASE_DAY_GTE, startDate);
        complexQuery.put(PRIMARY_RELEASE_DAY_LTE, endDate);

        return mApiService.getOngoingMovies(complexQuery)
                .toFlowable()
                .map(MovieResponse::getMovieList)
                .flatMap(Flowable::fromIterable)
                .doOnNext(movie-> movie.setOngoing(true))
                .toList()
                .toFlowable();
    }

    @VisibleForTesting
    public Flowable<List<Movie>> reloadUpcomingMovies() {
        HashMap<String, String> complexQuery = new HashMap<>();

        String startDate = mDateProvider.getStartMovieDateForUpcoming();
        String endDate = mDateProvider.getEndMovieDateForUpcoming();

        complexQuery.put(PRIMARY_RELEASE_DAY_GTE, startDate);
        complexQuery.put(PRIMARY_RELEASE_DAY_LTE, endDate);

        return mApiService.getUpcomingMovies(complexQuery)
                .toFlowable()
                .map(MovieResponse::getMovieList)
                .flatMap(Flowable::fromIterable)
                .doOnNext(movie -> movie.setUpcoming(true))
                .toList()
                .toFlowable();
    }

    @Override
    public Flowable<List<Movie>> reloadMoviesWithFlag(String flag) {
        if (!mLoadMoviesFunctions.containsKey(flag)) {
            throw new IllegalArgumentException("Can not load movies. This flag of movies does not allowed or does not exist");
        }

        return mLoadMoviesFunctions.get(flag).loadMovies();
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
}
