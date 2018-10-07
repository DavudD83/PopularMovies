package space.dotcat.popularmovies.screen.movies.fragments;

import java.util.List;

import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.screen.base.BaseDiffUtilCallback;

public class MoviesDiffUtilCallback extends BaseDiffUtilCallback<Movie> {

    public MoviesDiffUtilCallback(List<Movie> oldItems, List<Movie> newItems) {
        super(oldItems, newItems);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Movie oldMovie = getOldItem(oldItemPosition);

        Movie newMovie = getNewItem(newItemPosition);

        return oldMovie.getId() == newMovie.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Movie oldMovie = getOldItem(oldItemPosition);

        Movie newMovie = getNewItem(newItemPosition);

        return oldMovie.getTitle().equals(newMovie.getTitle());
    }
}
