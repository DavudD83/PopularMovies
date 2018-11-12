package space.dotcat.popularmovies.screen.movies.fragments.popularMovies;

import android.arch.lifecycle.ViewModelProviders;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesSwipeRefreshFragment;


public class PopularMoviesFragment extends BaseMoviesSwipeRefreshFragment<PopularMoviesViewModel> {

    public static PopularMoviesFragment create() {
        return new PopularMoviesFragment();
    }

    public PopularMoviesFragment() {
    }

    @Override
    protected PopularMoviesViewModel createViewModel() {
        return ViewModelProviders.of(this, mFactory)
                .get(PopularMoviesViewModel.class);
    }

    @Override
    protected void setupErrorMessage() {
        mEmptyErrorMessage.setText(R.string.empty_popular_movies);
    }
}
