package space.dotcat.popularmovies.screen.movies.fragments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies.FavoriteMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies.OngoingMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.popularMovies.PopularMoviesViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies.UpcomingMoviesViewModel;
import space.dotcat.popularmovies.utils.updatePeriodCalculator.FlexIntervalCalculator;

public class MoviesViewModelFactory implements ViewModelProvider.Factory {

    private MoviesRepository mMoviesRepository;

    private KeyValueRepository mKeyValueRepository;

    private Scheduler mScheduler;

    private FlexIntervalCalculator mFlexIntervalCalculator;

    @Inject
    public MoviesViewModelFactory(MoviesRepository moviesRepository, KeyValueRepository keyValueRepository,
                                  Scheduler scheduler, FlexIntervalCalculator flexIntervalCalculator) {
        mMoviesRepository = moviesRepository;

        mKeyValueRepository = keyValueRepository;

        mScheduler = scheduler;

        mFlexIntervalCalculator = flexIntervalCalculator;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(PopularMoviesViewModel.class)) {
            return (T) new PopularMoviesViewModel(mMoviesRepository, mScheduler, mKeyValueRepository,
                    mFlexIntervalCalculator);
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
