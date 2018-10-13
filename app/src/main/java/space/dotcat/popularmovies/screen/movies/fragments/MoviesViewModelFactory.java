package space.dotcat.popularmovies.screen.movies.fragments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies.FavoriteMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies.OngoingMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.popularMovies.PopularMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies.UpcomingMoviesViewModel;

public class MoviesViewModelFactory implements ViewModelProvider.Factory {

    private MoviesRepository mMoviesRepository;

    private Scheduler mScheduler;

    @Inject
    public MoviesViewModelFactory(MoviesRepository moviesRepository, Scheduler scheduler) {
        mMoviesRepository = moviesRepository;

        mScheduler = scheduler;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(PopularMoviesViewModel.class)) {
            return (T) new PopularMoviesViewModel(mMoviesRepository, mScheduler);
        } else if (modelClass.equals(OngoingMoviesViewModel.class)) {
            return (T) new OngoingMoviesViewModel(mMoviesRepository, mScheduler);
        } else if (modelClass.equals(UpcomingMoviesViewModel.class)) {
            return (T) new UpcomingMoviesViewModel(mMoviesRepository, mScheduler);
        } else if (modelClass.equals(FavoriteMoviesViewModel.class)) {
            return (T) new FavoriteMoviesViewModel(mMoviesRepository);
        }

        throw new IllegalArgumentException("There is no code for providing view model with such name " + modelClass.getName());
    }
}
