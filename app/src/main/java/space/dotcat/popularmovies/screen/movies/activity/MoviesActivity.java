package space.dotcat.popularmovies.screen.movies.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.base.BaseActivity;
import space.dotcat.popularmovies.screen.base.BaseFragment;
import space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies.FavoriteMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies.OngoingMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.popularMovies.PopularMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies.UpcomingMoviesFragment;
import space.dotcat.popularmovies.utils.BottomNavigationUtils;

public class MoviesActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tb_app_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.btv_navigation_view)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        setupNavigationView();

        if (savedInstanceState == null) {
            addFragment(R.id.fl_fragment_container, PopularMoviesFragment.create());
        }
    }

    @Override
    protected void onSetupActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.app_name));

        setToolbar(mToolbar);

        super.onSetupActionBar();
    }

    private void setupNavigationView() {
        BottomNavigationUtils.removeShiftMode(mBottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        BaseFragment baseFragment = (BaseFragment) mFragmentManager.findFragmentById(R.id.fl_fragment_container);

        baseFragment.resetError();

        switch(id) {
            case R.id.mi_popular_movies: {
                addFragment(R.id.fl_fragment_container, PopularMoviesFragment.create());

                return true;
            }

            case R.id.mi_ongoing_movies: {
                addFragment(R.id.fl_fragment_container, OngoingMoviesFragment.create());

                return true;
            }

            case R.id.mi_upcoming_movies: {
                addFragment(R.id.fl_fragment_container, UpcomingMoviesFragment.create());

                return true;
            }

            case R.id.mi_favorite_movies: {
                addFragment(R.id.fl_fragment_container, FavoriteMoviesFragment.create());
            }

            default: {
                return true;
            }
        }
    }
}
