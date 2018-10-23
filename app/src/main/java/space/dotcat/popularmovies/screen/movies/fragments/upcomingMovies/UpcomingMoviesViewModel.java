package space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesInternetViewModel;


public class UpcomingMoviesViewModel extends BaseMoviesInternetViewModel {

    public UpcomingMoviesViewModel(MoviesRepository moviesRepository, Scheduler scheduler) {
        super(moviesRepository, scheduler);
    }

    @Override
    public MutableLiveData<List<Movie>> getMovies() {
       return getMovies(Movie.FLAG_UPCOMING);
    }

    @Override
    public void getMoviesSortedByRating() {
        getMoviesWithFlagSortedByRating(Movie.FLAG_UPCOMING);
    }

    @Override
    public void getMoviesSortedByPopularity() {
        getMoviesWithFlagSortedByRating(Movie.FLAG_UPCOMING);
    }

    @Override
    public void reloadMovies() {
        reloadMovies(Movie.FLAG_UPCOMING);
    }

    @Override
    public void startSchedulingJob() {
        mScheduler.startUpdatingUpcomingMovies();
    }

    public void getMoviesSortedByDate() {
        mDisposables.clear();

        Disposable disposable = mMoviesRepository.getMoviesWithFlagSortedByDate(Movie.FLAG_UPCOMING)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> mMovies.setValue(movies),

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(disposable);
    }
}
