package space.dotcat.popularmovies.di.appLayer;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.MoviesDao;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.MoviesDatabase;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    MoviesDatabase provideMoviesDatabase(Context context) {
        return Room.inMemoryDatabaseBuilder(context, MoviesDatabase.class)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    MoviesDao provideMoviesDao(MoviesDatabase moviesDatabase) {
        return moviesDatabase.getMoviesDao();
    }
}
