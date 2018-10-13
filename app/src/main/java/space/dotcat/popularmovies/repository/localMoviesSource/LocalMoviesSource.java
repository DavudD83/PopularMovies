package space.dotcat.popularmovies.repository.localMoviesSource;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;

public interface LocalMoviesSource {

    /**
     * Get all movies from the database where the given flag equals to 1.
     *
     * @param flag flag that indicated type of movie. Should be from the strict list of constants from {@link Movie}
     *             Only these constants are allowed for now:
     *             1) {@link Movie#FLAG_ONGOING}
     *             2) {@link Movie#FLAG_POPULAR}
     *             3) {@link Movie#FLAG_UPCOMING}
     *             4) {@link Movie#FLAG_FAVORITE}
     *
     * @return
     */

    Flowable<List<Movie>> getMoviesByFlag(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByRating(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByPopularity(String flag);

    Flowable<List<Movie>> getMoviesWithFlagSortedByReleaseDate(String flag);

    void addMoviesSync(List<Movie> movies);

    LiveData<Movie> getMovieById(int movieId);

    Single<Video> getTrailer(int movieId);

    Single<List<Review>> getReviews(int movieId);

    Single<MovieExtraInfo> getTrailersAndReviews(int movieId);

    void addTrailerSync(Video ... videos);

    void addReviewsSync(List<Review> reviews);

    Completable updateMovie(Movie movie);

    /**
     * Updates movies in the database with the given movies. Algorithm is the following :
     * 1) Set all movies given flag to false
     * 2) Tries to insert movie one by one
     * 3) When there is a movie in the db so far, then it updates the content of the movie except of the flags
     * 4) Eventually, updates the flag of inserted or updated movies to true
     *
     * @param movies new reloaded movies from the internet
     * @param flag flag that indicated type of movie. Should be from the strict list of constants from {@link Movie}
     *             Only these constants are allowed for now :
     *             1) {@link Movie#FLAG_ONGOING}
     *             2) {@link Movie#FLAG_POPULAR}
     *             3) {@link Movie#FLAG_UPCOMING}
     *             4) {@link Movie#FLAG_FAVORITE}
     *
     */

    void updateReloadedMoviesSync(List<Movie> movies, String flag);

    /**
     * Method for testing purposes
     *
     */

    void deleteAllMovies();

    int deleteMoviesWithoutFlags();
}
