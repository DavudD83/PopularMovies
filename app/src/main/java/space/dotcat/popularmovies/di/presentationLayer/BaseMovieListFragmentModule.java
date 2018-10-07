package space.dotcat.popularmovies.di.presentationLayer;

import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import space.dotcat.popularmovies.screen.movies.fragments.MoviesViewModelFactory;

@Module
public abstract class BaseMovieListFragmentModule {

    @Binds
    abstract ViewModelProvider.Factory provideMovieListViewModelFactory(MoviesViewModelFactory movieListViewModelFactory);
}
