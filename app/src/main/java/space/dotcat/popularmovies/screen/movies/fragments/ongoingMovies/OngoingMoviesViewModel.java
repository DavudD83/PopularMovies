package space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesInternetViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesViewModel;

public class OngoingMoviesViewModel extends BaseMoviesInternetViewModel {

    public OngoingMoviesViewModel(MoviesRepository moviesRepository) {
        super(moviesRepository);
    }

    @Override
    public MutableLiveData<List<Movie>> getMovies() {
       return getMovies(Movie.FLAG_ONGOING);
    }

    @Override
    public void getMoviesSortedByRating() {
        getMoviesWithFlagSortedByRating(Movie.FLAG_ONGOING);
    }

    @Override
    public void getMoviesSortedByPopularity() {
        getMoviesWithFlagSortedByPopularity(Movie.FLAG_ONGOING);
    }

    @Override
    public void reloadMovies() {
       reloadMovies(Movie.FLAG_ONGOING);
    }

    public void getMoviesSortedByDate() {
        mDisposables.clear();

        Disposable disposable = mMoviesRepository.getMoviesWithFlagSortedByDate(Movie.FLAG_ONGOING)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> mMovies.setValue(movies),

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(disposable);
    }
}
