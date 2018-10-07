package space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies;

import android.arch.lifecycle.ViewModelProviders;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesSwipeRefreshFragment;

public class OngoingMoviesFragment extends BaseMoviesSwipeRefreshFragment<OngoingMoviesViewModel> {

    private static final int SORT_MOVIES_BY_DATE_ID = 100;

    public static OngoingMoviesFragment create() {
        return new OngoingMoviesFragment();
    }

    public OngoingMoviesFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, SORT_MOVIES_BY_DATE_ID, Menu.NONE, R.string.sort_movie_by_date);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == SORT_MOVIES_BY_DATE_ID) {
            mViewModel.getMoviesSortedByDate();

            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupErrorMessage() {
        mEmptyErrorMessage.setText(getString(R.string.empty_ongoing_movies));
    }

    @Override
    protected OngoingMoviesViewModel createViewModel() {
        return ViewModelProviders.of(this, mFactory).get(OngoingMoviesViewModel.class);
    }
}
