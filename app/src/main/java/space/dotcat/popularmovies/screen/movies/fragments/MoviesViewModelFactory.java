package space.dotcat.popularmovies.screen.movies.fragments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies.FavoriteMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies.OngoingMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.popularMovies.PopularMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies.UpcomingMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies.UpcomingMoviesViewModel;

public class MoviesViewModelFactory implements ViewModelProvider.Factory {

    private MoviesRepository mMoviesRepository;

    @Inject
    public MoviesViewModelFactory(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(PopularMoviesViewModel.class)) {
            return (T) new PopularMoviesViewModel(mMoviesRepository);
        } else if (modelClass.equals(OngoingMoviesViewModel.class)) {
            return (T) new OngoingMoviesViewModel(mMoviesRepository);
        } else if (modelClass.equals(UpcomingMoviesViewModel.class)) {
            return (T) new UpcomingMoviesViewModel(mMoviesRepository);
        } else if (modelClass.equals(FavoriteMoviesViewModel.class)) {
            return (T) new FavoriteMoviesViewModel(mMoviesRepository);
        }

        throw new IllegalArgumentException("There is no code for providing view model with such name " + modelClass.getName());
    }
}
