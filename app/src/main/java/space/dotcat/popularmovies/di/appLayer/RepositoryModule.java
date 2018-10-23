package space.dotcat.popularmovies.di.appLayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.api.ApiService;
import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;
import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepositoryImpl;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepositoryImpl;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.LocalMoviesSource;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.LocalMoviesSourceImpl;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.MoviesDao;
import space.dotcat.popularmovies.repository.moviesRepository.remoteMoviesSource.RemoteMoviesSource;
import space.dotcat.popularmovies.repository.moviesRepository.remoteMoviesSource.RemoteMoviesSourceImpl;
import space.dotcat.popularmovies.utils.date.DateProvider;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    LocalMoviesSource provideLocalDataSource(MoviesDao moviesDao) {
        return new LocalMoviesSourceImpl(moviesDao);
    }

    @Provides
    @Singleton
    RemoteMoviesSource provideRemoteDataSource(ApiService apiService, DateProvider dateProvider) {
        return new RemoteMoviesSourceImpl(apiService, dateProvider);
    }

    @Provides
    @Singleton
    MoviesRepository provideMoviesRepository(LocalMoviesSource localDataSource,
                                             RemoteMoviesSource remoteDataSource) {
        return new MoviesRepositoryImpl(localDataSource, remoteDataSource);
    }

    @Provides
    @Singleton
    KeyValueRepository provideKeyValueRepository(SharedPreferences sharedPreferences) {
        return new KeyValueRepositoryImpl(sharedPreferences);
    }
}
