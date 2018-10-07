package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.screen.movies.fragments.MoviesAdapter;
import space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies.FavoriteMoviesFragment;
import space.dotcat.popularmovies.utils.image.ImageLoader;

@Module(includes = BaseMovieListFragmentModule.class)
public abstract class FavoriteMoviesFragmentModule {

    @Provides
    static MoviesAdapter provideAdapter(ImageLoader imageLoader, FavoriteMoviesFragment favoriteMoviesFragment) {
        ImageSize imageSize = favoriteMoviesFragment.getScreenSize();

        return new MoviesAdapter(imageLoader, imageSize, favoriteMoviesFragment);
    }
}
