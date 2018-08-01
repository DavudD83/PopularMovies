package space.dotcat.popularmovies.di.appLayer;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.repository.MoviesDatabase;
import space.dotcat.popularmovies.repository.MoviesDao;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    MoviesDatabase provideMoviesDatabase(Context context) {
        return Room.databaseBuilder(context, MoviesDatabase.class, "MoviesDatabase")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    MoviesDao provideMoviesDao(MoviesDatabase moviesDatabase) {
        return moviesDatabase.getMoviesDao();
    }
}
