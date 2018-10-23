package space.dotcat.popularmovies.di.presentationLayer.popularMovies;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.di.presentationLayer.BaseMovieListFragmentModule;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.screen.movies.fragments.MoviesAdapter;
import space.dotcat.popularmovies.screen.movies.fragments.popularMovies.PopularMoviesFragment;
import space.dotcat.popularmovies.utils.image.ImageLoader;

@Module(includes = BaseMovieListFragmentModule.class)
public abstract class PopularMoviesFragmentModule {

    @Provides
    static MoviesAdapter providePopularMoviesAdapter(ImageLoader imageLoader,
                                                     PopularMoviesFragment popularMoviesFragment) {
        ImageSize imageSize = popularMoviesFragment.getScreenSize();

        return new MoviesAdapter(imageLoader, imageSize, popularMoviesFragment);
    }
}
