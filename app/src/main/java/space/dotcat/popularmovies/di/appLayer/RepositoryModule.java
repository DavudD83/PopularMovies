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
import space.dotcat.popularmovies.repository.localMoviesSource.LocalMoviesSourceImpl;
import space.dotcat.popularmovies.repository.MoviesDao;
import space.dotcat.popularmovies.repository.remoteMoviesSource.RemoteMoviesSourceImpl;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    @Local
    MoviesRepository provideLocalDataSource(MoviesDao moviesDao) {
        return new LocalMoviesSourceImpl(moviesDao);
    }

    @Provides
    @Singleton
    @Remote
    MoviesRepository provideRemoteDataSource(ApiService apiService) {
        return new RemoteMoviesSourceImpl(apiService);
    }

    @Provides
    @Singleton
    @Main
    MoviesRepository provideMoviesRepository(@Local MoviesRepository localDataSource,
                                             @Remote MoviesRepository remoteDataSource) {
        return new MoviesRepositoryImpl(localDataSource, remoteDataSource);
    }
}
