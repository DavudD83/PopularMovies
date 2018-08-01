package space.dotcat.popularmovies.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;

@Database(entities = {Movie.class, Review.class, Video.class}, version = 9, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    public abstract MoviesDao getMoviesDao();
}
