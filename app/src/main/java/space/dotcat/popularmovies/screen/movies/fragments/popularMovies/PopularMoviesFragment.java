package space.dotcat.popularmovies.screen.movies.fragments.popularMovies;

import android.arch.lifecycle.ViewModelProviders;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesSwipeRefreshFragment;


public class PopularMoviesFragment extends BaseMoviesSwipeRefreshFragment<PopularMoviesViewModel> {

    public static PopularMoviesFragment create() {
        return new PopularMoviesFragment();
    }

    public PopularMoviesFragment() {
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.mi_sort_by_popularity: {
//                mViewModel.sortMoviesByPopularity();
//
//                return true;
//            }
//
//            case R.id.mi_sort_by_rating: {
//                mViewModel.sortMoviesByRating();
//
//                return true;
//            }
//
//            default: {
//                return false;
//            }
//        }
//    }

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
