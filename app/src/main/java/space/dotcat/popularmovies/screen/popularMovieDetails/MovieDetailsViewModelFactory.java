package space.dotcat.popularmovies.screen.popularMovieDetails;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.MoviesRepository;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {

    private int mMovieId;

    @Inject
    @Main
    MoviesRepository mMoviesRepository;

    public MovieDetailsViewModelFactory(int movieId, MoviesRepository moviesRepository) {
        mMovieId = movieId;
        mMoviesRepository = moviesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(mMovieId, mMoviesRepository);
    }
}
