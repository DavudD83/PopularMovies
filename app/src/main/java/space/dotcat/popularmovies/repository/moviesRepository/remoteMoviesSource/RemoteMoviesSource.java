package space.dotcat.popularmovies.repository.moviesRepository.remoteMoviesSource;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;

public interface RemoteMoviesSource {

    Flowable<List<Movie>> reloadMoviesWithFlag(String flag);

    Single<List<Video>> getTrailer(int movieId);

    Single<List<Review>> getReviews(int movieId);

    Single<MovieExtraInfo> getTrailersAndReviews(int movieId);
}
