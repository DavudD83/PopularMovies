package space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesSwipeRefreshFragment;


public class UpcomingMoviesFragment extends BaseMoviesSwipeRefreshFragment<UpcomingMoviesViewModel> {

    private static final int SORT_MOVIES_BY_DATE_ID = 100;

    public UpcomingMoviesFragment() {
    }

    public static Fragment create() {
        return new UpcomingMoviesFragment();
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
        mEmptyErrorMessage.setText(getString(R.string.empty_upcoming_movies));
    }

    @Override
    protected UpcomingMoviesViewModel createViewModel() {
        return ViewModelProviders.of(this, mFactory).get(UpcomingMoviesViewModel.class);
    }
}
