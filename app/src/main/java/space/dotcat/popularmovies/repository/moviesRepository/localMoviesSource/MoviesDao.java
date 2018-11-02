package space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.support.annotation.VisibleForTesting;

import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;

@Dao
public abstract class MoviesDao {

    public static final String QUERY_SET_ALL_MOVIES_FLAG_TO_FALSE = "Update Movies set %s = 0";

    public static final String QUERY_SET_MOVIE_FLAG_TO_TRUE = "Update Movies set %s = 1 where movie_id = %d";

    private static final String QUERY_GET_MOVIES_BY_FLAG = "Select * from Movies where %s = 1";

    private static final String QUERY_GET_MOVIES_WITH_FLAG_SORTED_BY_RATING =
            "Select * from Movies where %s = 1 order by movie_average_rating DESC";

    private static final String QUERY_GET_MOVIES_WITH_FLAG_SORTED_BY_POPULARITY =
            "Select * from Movies where %s = 1 order by movie_popularity DESC";

    private static final String QUERY_GET_MOVIES_WITH_FLAG_SORTED_BY_DATE =
            "Select * from Movies where %s = 1 order by movie_release_date ASC";

    private static final int NOT_SUCCESSFUL = -1;

    @RawQuery(observedEntities = Movie.class)
    abstract Flowable<List<Movie>> getMovies(SupportSQLiteQuery query);

    public Flowable<List<Movie>> getMoviesByFlag(String flag) {
        String query_get_movies_by_type = String.format(QUERY_GET_MOVIES_BY_FLAG, flag);

        SupportSQLiteQuery query = new SimpleSQLiteQuery(query_get_movies_by_type);

        return getMovies(query);
    }

    @RawQuery(observedEntities = Movie.class)
    abstract Flowable<List<Movie>> getSortedMoviesByQuery(SupportSQLiteQuery query);

    /**
     * Load all movies with flag sorted by rating
     *
     * @param flag - flag that indicates the movie type
     * @return observable stream of movies
     */

    Flowable<List<Movie>> getMoviesWithFlagSortedByRating(String flag) {
        String string_query = String.format(QUERY_GET_MOVIES_WITH_FLAG_SORTED_BY_RATING, flag);

        SupportSQLiteQuery getMoviesQuery = new SimpleSQLiteQuery(string_query);

        return getSortedMoviesByQuery(getMoviesQuery);
    }

    /**
     * Load all movies with flag sorted by popularity
     *
     * @param flag - flag that indicates the movie type
     * @return observable stream of movies
     */

    Flowable<List<Movie>> getMoviesWithFlagSortedByPopularity(String flag) {
        String string_query = String.format(QUERY_GET_MOVIES_WITH_FLAG_SORTED_BY_POPULARITY, flag);

        SupportSQLiteQuery getMoviesQuery = new SimpleSQLiteQuery(string_query);

        return getSortedMoviesByQuery(getMoviesQuery);
    }

    Flowable<List<Movie>> getMoviesWithFlagSortedByDate(String flag) {
        String string_query = String.format(QUERY_GET_MOVIES_WITH_FLAG_SORTED_BY_DATE, flag);

        SupportSQLiteQuery getMoviesQuery = new SimpleSQLiteQuery(string_query);

        return getSortedMoviesByQuery(getMoviesQuery);
    }

    @Query("Select * from Movies where movie_id = :movieId")
    abstract LiveData<Movie> getMovieById(int movieId);

    @Query("Select * from Videos where movie_id = :movieId")
    abstract Single<List<Video>> getTrailer(int movieId);

    @Query("Select * from Reviews where mMovieId = :movieId")
    abstract Single<List<Review>> getReviews(int movieId);

//    @Transaction
//    @Query("Select * from Videos, Reviews where Videos.movie_id = :movieId and Reviews.mMovieId = :movieId")
//    abstract Flowable<MovieExtraInfo> getMovieExtraInfo(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract void insertTrailer(Video ... video);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract void insertReviews(List<Review> reviews);

    @Update
    abstract void updateMovie(Movie movie);

    @Query("Update Movies set movie_average_rating = :vote_average, movie_title = :title," +
            "movie_popularity = :popularity, movie_poster_path = :posterPath, movie_original_language = :originalLanguage," +
            "movie_overview = :overview, movie_release_date = :releaseDate WHERE movie_id = :movieId")
    abstract void updateMovieContent(int movieId, float vote_average, String title, float popularity, String posterPath,
                                  String originalLanguage, String overview, String releaseDate);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertMovie(Movie movie);

    @RawQuery
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract long setAllMoviesFlagToFalse(SupportSQLiteQuery supportSQLiteQuery);

    @RawQuery
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract long setMovieFlagToTrue(SupportSQLiteQuery supportSQLiteQuery);

    @Transaction
    void updateReloadedMovies(List<Movie> movies, String flag) {
        String query_set_all_movies_flag_to_false = String.format(QUERY_SET_ALL_MOVIES_FLAG_TO_FALSE, flag);

        SupportSQLiteQuery querySetAllMoviesFlagToFalse = new SimpleSQLiteQuery(query_set_all_movies_flag_to_false);

        setAllMoviesFlagToFalse(querySetAllMoviesFlagToFalse);

        for (Movie movie: movies) {
            long isSuccessful = insertMovie(movie);

            if (isSuccessful == NOT_SUCCESSFUL) {
                updateMovieContent(movie.getId(), movie.getVote_average(), movie.getTitle(), movie.getPopularity(),
                        movie.getPoster_path(), movie.getOriginal_language(), movie.getOverview(), movie.getReleaseDate());

                String query_set_movie_flag_to_true = String.format(Locale.ENGLISH,
                        QUERY_SET_MOVIE_FLAG_TO_TRUE, flag, movie.getId());

                SupportSQLiteQuery querySetMovieFlagToTrue = new SimpleSQLiteQuery(query_set_movie_flag_to_true);

                setMovieFlagToTrue(querySetMovieFlagToTrue);
            }
        }
    }

    @Query("Delete from Movies where movie_is_popular = 0 and movie_is_ongoing = 0 " +
            "and movie_is_upcoming = 0 and movie_is_favorite = 0")
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract int deleteMoviesWithoutFlags();

    /**
     * Method for testing purposes.
     *
     */

    @Query("Delete from Movies")
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract void deleteAllMovies();

    /**
     * Method for testing purposes
     *
     * @param movies - list of movies
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract void insertMovies(List<Movie> movies);

    @Query("Select count(movie_id) from Movies")
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    public abstract int getMoviesCount();
}
