package space.dotcat.popularmovies.screen.movieDetails.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.screen.base.BaseViewModel;
import space.dotcat.popularmovies.screen.movieDetails.Refreshable;

public class MovieDetailsViewModel extends BaseViewModel {

    private static final String TAG = MovieDetailsViewModel.class.getName();

    public static final int LOADING_ERROR = 1;

    private int mMovieId;

    private LiveData<Movie> mMovieLiveData;

    private MutableLiveData<MovieExtraInfo> mMovieExtraInfoLiveData;

    private MoviesRepository mMoviesRepository;

    public MovieDetailsViewModel(int movieId, MoviesRepository moviesRepository) {
        mMovieId = movieId;

        mMoviesRepository = moviesRepository;
    }

    public LiveData<Movie> getMovie() {
        if (mMovieLiveData == null) {
            mMovieLiveData = new MutableLiveData<>();

            mMovieLiveData = mMoviesRepository.getMovieById(mMovieId);
        }

        return mMovieLiveData;
    }

    public LiveData<MovieExtraInfo> getTrailerAndReviews() {
        if (mMovieExtraInfoLiveData == null) {
            mMovieExtraInfoLiveData = new MutableLiveData<>();

            loadTrailersAndReviews();
        }

        return mMovieExtraInfoLiveData;
    }

    public void updateMovie(boolean favorite_state) {
        Movie movie = mMovieLiveData.getValue();

        if (movie == null) {
            Log.d(TAG, "Can not update null movie");

            return;
        }

        movie.setIsFavorite(favorite_state);

        Disposable disposable = mMoviesRepository.updateMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},

                        throwable -> mError.setValue(Error.create(throwable))
                );

        mDisposables.add(disposable);
    }

    public void loadTrailersAndReviews() {
        Disposable d = mMoviesRepository.getTrailersAndReviews(mMovieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .doOnSubscribe(disposable -> mLoading.postValue(true))
                .doAfterTerminate(()-> mLoading.postValue(false))
                .subscribe(
                        movieExtraInfo -> mMovieExtraInfoLiveData.setValue(movieExtraInfo),

                        throwable -> mError.setValue(Error.create(throwable, LOADING_ERROR))
                );

        mDisposables.add(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mDisposables.clear();
    }
}
