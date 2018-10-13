package space.dotcat.popularmovies.screen.movies.fragments;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.screen.base.BaseViewModel;


public abstract class BaseMoviesViewModel extends BaseViewModel {

    protected MutableLiveData<List<Movie>> mMovies;

    protected MoviesRepository mMoviesRepository;

    public BaseMoviesViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;

    }

    public abstract MutableLiveData<List<Movie>> getMovies();

    public abstract void getMoviesSortedByRating();

    protected void getMoviesWithFlagSortedByRating(String flag) {
        mDisposables.clear();

        Disposable disposable = mMoviesRepository.getMoviesWithFlagSortedByRating(flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> mMovies.setValue(movies),

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(disposable);
    }

    public abstract void getMoviesSortedByPopularity();

    protected void getMoviesWithFlagSortedByPopularity(String flag) {
        mDisposables.clear();

        Disposable disposable = mMoviesRepository.getMoviesWithFlagSortedByPopularity(flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> mMovies.setValue(movies),

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(disposable);
    }
}
