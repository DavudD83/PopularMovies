package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.popularMovies.PopularMoviesAdapter;
import space.dotcat.popularmovies.screen.popularMovies.PopularMoviesViewModelFactory;

@Module
public class PopularMoviesActivityModule {

    private ImageSize mImageSize;

    private PopularMoviesAdapter.OnMovieClickListener mOnMovieClickListener;

    public PopularMoviesActivityModule(ImageSize imageSize,
                                       PopularMoviesAdapter.OnMovieClickListener onMovieClickListener) {
        mImageSize = imageSize;
        mOnMovieClickListener = onMovieClickListener;
    }

    @Provides
    @ActivityScope
    PopularMoviesViewModelFactory providePopularMoviesFactory(@Main MoviesRepository moviesRepository) {
        return new PopularMoviesViewModelFactory(moviesRepository);
    }

    @Provides
    @ActivityScope
    PopularMoviesAdapter providePopularMoviesAdapter() {
        return new PopularMoviesAdapter(mImageSize, mOnMovieClickListener);
    }
}
