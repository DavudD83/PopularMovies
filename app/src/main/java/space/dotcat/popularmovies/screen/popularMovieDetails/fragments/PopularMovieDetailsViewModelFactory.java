package space.dotcat.popularmovies.screen.popularMovieDetails.fragments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.popularMovieDetails.fragments.PopularMovieDetailsViewModel;

public class PopularMovieDetailsViewModelFactory implements ViewModelProvider.Factory {

    private int mMovieId;

    MoviesRepository mMoviesRepository;

    public PopularMovieDetailsViewModelFactory(int movieId, MoviesRepository moviesRepository) {
        mMovieId = movieId;

        mMoviesRepository = moviesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PopularMovieDetailsViewModel(mMovieId, mMoviesRepository);
    }
}
