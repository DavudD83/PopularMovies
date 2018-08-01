package space.dotcat.popularmovies.screen.popularMovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.base.BaseViewModel;

public class PopularMoviesViewModel extends BaseViewModel {

    private MutableLiveData<List<Movie>> mMovies;

    private MoviesRepository mMoviesRepository;

    @Inject
    public PopularMoviesViewModel(@Main MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public LiveData<List<Movie>> getPopularMovies() {
         if(mMovies == null) {
             mMovies = new MutableLiveData<>();

             Disposable moviesFromDb = mMoviesRepository.getPopularMovies()
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .doOnSubscribe(subscription -> mLoading.postValue(true))
                     .subscribe(
                             movies -> {
                                 if (movies.isEmpty()) {
                                     reloadMovies();
                                 }

                                 mLoading.setValue(false);

                                 mMovies.setValue(movies);
                             },

                             throwable -> {
                                 mLoading.setValue(false);
                                 mError.setValue(Error.create(throwable));
                             }
                     );

             mDisposables.add(moviesFromDb);
         }

         return mMovies;
    }

    public void sortMoviesByRating() {
        mDisposables.clear();

        Disposable d = mMoviesRepository.getPopularMoviesSortedByRating()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> mMovies.setValue(movies),

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(d);
    }

    public void sortMoviesByPopularity() {
        mDisposables.clear();

        Disposable d = mMoviesRepository.getPopularMoviesSortedByPopularity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> mMovies.setValue(movies),

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(d);
    }

    public void getFavoriteMovies() {
        mDisposables.clear();

        Disposable d = mMoviesRepository.getFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> mMovies.setValue(movies),

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(d);
    }

    public void reloadMovies() {
        Disposable d = mMoviesRepository.reloadMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> {},

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mDisposables.clear();
    }
}
