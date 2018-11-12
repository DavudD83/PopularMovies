package space.dotcat.popularmovies.screen.movies.fragments.popularMovies;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import space.dotcat.popularmovies.model.FlexInterval;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesInternetViewModel;
import space.dotcat.popularmovies.utils.updatePeriodCalculator.FlexIntervalCalculator;

public class PopularMoviesViewModel extends BaseMoviesInternetViewModel {

    private KeyValueRepository mKeyValueRepository;

    private FlexIntervalCalculator mFlexIntervalCalculator;

    public PopularMoviesViewModel(MoviesRepository moviesRepository, Scheduler scheduler,
                                  KeyValueRepository keyValueRepository, FlexIntervalCalculator flexIntervalCalculator) {
        super(moviesRepository, scheduler);

        mFlexIntervalCalculator = flexIntervalCalculator;

        mKeyValueRepository = keyValueRepository;
    }

    @Override
    public void startSchedulingJob() {
        mScheduler.startDeletingUnflagedMovies();

        long period_of_updating = mKeyValueRepository.getPeriodOfUpdatingPopularMovies();

        FlexInterval flexInterval = mFlexIntervalCalculator.calculateFlexInterval(period_of_updating);

        mScheduler.startUpdatingPopularMovies(period_of_updating, flexInterval.getFlexInterval(),
                flexInterval.getTimeUnit());
    }

    @Override
    public MutableLiveData<List<Movie>> getMovies() {
       return getMovies(Movie.FLAG_POPULAR);
    }

    @Override
    public void reloadMovies() {
        reloadMovies(Movie.FLAG_POPULAR);
    }

    @Override
    public void getMoviesSortedByRating() {
        getMoviesWithFlagSortedByRating(Movie.FLAG_POPULAR);
    }

    @Override
    public void getMoviesSortedByPopularity() {
        getMoviesWithFlagSortedByPopularity(Movie.FLAG_POPULAR);
    }
}
