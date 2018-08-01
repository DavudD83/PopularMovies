package space.dotcat.popularmovies.screen.popularMovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.popularMovies.PopularMoviesViewModel;

public class PopularMoviesViewModelFactory implements ViewModelProvider.Factory {

    private MoviesRepository mMoviesRepository;

    @Inject
    public PopularMoviesViewModelFactory(@Main MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PopularMoviesViewModel(mMoviesRepository);
    }
}
