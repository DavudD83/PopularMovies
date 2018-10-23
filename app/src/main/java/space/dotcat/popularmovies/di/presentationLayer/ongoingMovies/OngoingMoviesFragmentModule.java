package space.dotcat.popularmovies.di.presentationLayer.ongoingMovies;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.di.presentationLayer.BaseMovieListFragmentModule;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.screen.movies.fragments.MoviesAdapter;
import space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies.OngoingMoviesFragment;
import space.dotcat.popularmovies.utils.image.ImageLoader;

@Module(includes = BaseMovieListFragmentModule.class)
public abstract class OngoingMoviesFragmentModule {

    @Provides
    static MoviesAdapter provideOngoingFragmentAdapter(ImageLoader imageLoader,
                                                       OngoingMoviesFragment ongoingMoviesFragment) {
        ImageSize imageSize = ongoingMoviesFragment.getScreenSize();

        return new MoviesAdapter(imageLoader, imageSize, ongoingMoviesFragment);
    }

}
