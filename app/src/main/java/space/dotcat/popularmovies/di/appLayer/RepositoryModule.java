package space.dotcat.popularmovies.di.appLayer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.api.ApiService;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Local;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Remote;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.repository.MoviesRepositoryImpl;
import space.dotcat.popularmovies.repository.localMoviesSource.LocalMoviesSource;
import space.dotcat.popularmovies.repository.localMoviesSource.LocalMoviesSourceImpl;
import space.dotcat.popularmovies.repository.localMoviesSource.MoviesDao;
import space.dotcat.popularmovies.repository.remoteMoviesSource.RemoteMoviesSource;
import space.dotcat.popularmovies.repository.remoteMoviesSource.RemoteMoviesSourceImpl;
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
}
