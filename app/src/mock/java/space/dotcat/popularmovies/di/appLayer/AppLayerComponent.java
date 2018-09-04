package space.dotcat.popularmovies.di.appLayer;

import javax.inject.Singleton;

import dagger.Component;
import space.dotcat.popularmovies.api.ApiService;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Remote;
import space.dotcat.popularmovies.di.presentationLayer.MovieDetailsActivityComponent;
import space.dotcat.popularmovies.di.presentationLayer.MovieDetailsActivityModule;
import space.dotcat.popularmovies.di.presentationLayer.PopularMoviesActivityComponent;
import space.dotcat.popularmovies.di.presentationLayer.PopularMoviesActivityModule;
import space.dotcat.popularmovies.repository.MoviesDao;
import space.dotcat.popularmovies.repository.MoviesRepository;

@Component(modules = {AppModule.class, DatabaseModule.class, NetworkModule.class, RepositoryModule.class})
@Singleton
public interface AppLayerComponent {

    PopularMoviesActivityComponent plusPopularMoviesComponent(PopularMoviesActivityModule moviesActivityModule);

    MovieDetailsActivityComponent plusMovieDetailsComponent(MovieDetailsActivityModule movieDetailsActivityModule);

    ApiService getFakeApiService();

    MoviesDao getFakeMoviesDao();
}
