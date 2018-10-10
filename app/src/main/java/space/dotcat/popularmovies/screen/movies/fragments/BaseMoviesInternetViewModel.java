package space.dotcat.popularmovies.screen.movies.fragments;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.MoviesRepository;

public abstract class BaseMoviesInternetViewModel extends BaseMoviesViewModel {

    public static final int INTERNET_CONNECTION_PROBLEM = 100;

    public BaseMoviesInternetViewModel(MoviesRepository moviesRepository) {
        super(moviesRepository);
    }

    protected MutableLiveData<List<Movie>> getMovies(String flag) {
        if (mMovies == null) {
            mMovies = new MutableLiveData<>();

            Disposable disposable = mMoviesRepository.getMoviesWithFlag(flag)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(subscription -> mLoading.postValue(true))
                    .subscribe(
                            movies -> {
                                if (movies.isEmpty()) {
                                    reloadMovies();
                                    return;
                                }

                                mLoading.setValue(false);

                                mMovies.setValue(movies);
                            },

                            throwable -> {
                                mLoading.setValue(false);

                                mError.setValue(Error.create(throwable));
                            }
                    );

            mDisposables.add(disposable);
        }

        return mMovies;
    }

    protected void reloadMovies(String flag) {
        Disposable disposable = mMoviesRepository.reloadMoviesWithFlag(flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(()-> mLoading.setValue(false))
                .subscribe(
                        movies -> {
                            if (movies.isEmpty()) {
                                mMovies.setValue(movies); // indicates that response from server was successful,
                                // but there is no movie in response, and we need to show msg to user
                            }
                        },

                        throwable -> {
                            mLoading.setValue(false);

                            mError.setValue(Error.create(throwable, INTERNET_CONNECTION_PROBLEM));
                        }
                );

        mDisposables.add(disposable);
    }

    public abstract void reloadMovies();
}
