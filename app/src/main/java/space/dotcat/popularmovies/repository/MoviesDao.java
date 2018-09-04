package space.dotcat.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;

@Dao
public interface MoviesDao {

    /**
     * Load all movies from database
     *
     * @return observable flowable which contains list of movies
     */

    @Query("Select * from Movies")
    Flowable<List<Movie>> getMovies();

    /**
     * Load all movies from database ordered by movie average rating descending
     *
     * @return observable flowable which contains list of movies ordered by movie average rating descending
     */

    @Query("Select * from Movies order by movie_average_rating DESC")
    Flowable<List<Movie>> getMoviesSortedByRating();

    /**
     * Load all movies from database ordered by movie popularity descending
     *
     * @return observable flowable which contains list of movies ordered by movie popularity descending
     */

    @Query("Select * from Movies order by movie_popularity DESC")
    Flowable<List<Movie>> getMoviesSortedByPopularity();

    @Query("Select * from Movies where movie_is_favorite = 1")
    Flowable<List<Movie>> getFavoriteMovies();

    @Query("Delete from Movies")
    void deleteAllMovies();

    @Insert
    void insertMovies(List<Movie> movies);

    @Query("Select * from Movies where movie_id = :movieId")
    LiveData<Movie> getMovieById(int movieId);

    @Query("Select * from Videos where movie_id = :movieId")
    Single<Video> getTrailer(int movieId);

    @Query("Select * from Reviews where mMovieId = :movieId")
    Single<List<Review>> getReviews(int movieId);

    @Transaction
    @Query("Select * from Videos, Reviews where Videos.movie_id = :movieId and Reviews.mMovieId = :movieId")
    Single<MovieExtraInfo> getMovieExtraInfo(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailer(Video ... video);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReviews(List<Review> reviews);

    @Update
    void updateMovie(Movie movie);
}
