package space.dotcat.popularmovies.di.presentationLayer.upcomingMovies;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.di.presentationLayer.BaseMovieListFragmentModule;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.screen.movies.fragments.MoviesAdapter;
import space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies.UpcomingMoviesFragment;
import space.dotcat.popularmovies.utils.image.ImageLoader;

@Module(includes = BaseMovieListFragmentModule.class)
public abstract class UpcomingMoviesFragmentModule {

    @Provides
    static MoviesAdapter provideMovieListAdapter(ImageLoader imageLoader,
                                                 UpcomingMoviesFragment upcomingMoviesFragment) {
        ImageSize imageSize = upcomingMoviesFragment.getScreenSize();

        return new MoviesAdapter(imageLoader, imageSize, upcomingMoviesFragment);
    }

}
