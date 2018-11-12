package space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
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
