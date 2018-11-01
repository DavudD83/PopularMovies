package space.dotcat.popularmovies.screen.movieDetails.fragments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {

    private int mMovieId;

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
