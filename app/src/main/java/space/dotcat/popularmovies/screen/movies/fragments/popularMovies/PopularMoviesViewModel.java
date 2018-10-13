package space.dotcat.popularmovies.screen.movies.fragments.popularMovies;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesInternetViewModel;

public class PopularMoviesViewModel extends BaseMoviesInternetViewModel {

    public PopularMoviesViewModel(MoviesRepository moviesRepository, Scheduler scheduler) {
        super(moviesRepository, scheduler);
    }

    @Override
    protected void startSchedulingJob() {
        mScheduler.startDeletingUnflagedMovies();

        mScheduler.startUpdatingPopularMovies();
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
