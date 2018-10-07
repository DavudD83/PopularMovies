package space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies;

import android.arch.lifecycle.ViewModelProviders;
import android.view.Menu;
import android.view.MenuInflater;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesFragment;


public class FavoriteMoviesFragment extends BaseMoviesFragment<FavoriteMoviesViewModel> {

    public static FavoriteMoviesFragment create() {
        return new FavoriteMoviesFragment();
    }

    @Override
    protected void setupErrorMessage() {
        mEmptyErrorMessage.setText(getString(R.string.empty_favorite_movies));
    }

    @Override
    protected FavoriteMoviesViewModel createViewModel() {
        return ViewModelProviders.of(this, mFactory).get(FavoriteMoviesViewModel.class);
    }
}
