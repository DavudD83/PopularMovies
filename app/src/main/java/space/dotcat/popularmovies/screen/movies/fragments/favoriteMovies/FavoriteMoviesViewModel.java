package space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesViewModel;


public class FavoriteMoviesViewModel extends BaseMoviesViewModel {


    public FavoriteMoviesViewModel(MoviesRepository moviesRepository) {
        super(moviesRepository);
    }

    @Override
    public MutableLiveData<List<Movie>> getMovies() {
        if (mMovies == null) {
            mMovies = new MutableLiveData<>();

            Disposable disposable = mMoviesRepository.getMoviesWithFlag(Movie.FLAG_FAVORITE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            movies -> {
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

    @Override
    public void getMoviesSortedByRating() {
        getMoviesWithFlagSortedByRating(Movie.FLAG_FAVORITE);
    }

    @Override
    public void getMoviesSortedByPopularity() {
        getMoviesWithFlagSortedByPopularity(Movie.FLAG_FAVORITE);
    }
}
