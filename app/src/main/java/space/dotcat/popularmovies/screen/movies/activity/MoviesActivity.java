package space.dotcat.popularmovies.screen.movies.activity;

import android.content.Context;
import android.content.Intent;
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
import space.dotcat.popularmovies.screen.settings.SettingsActivity;
import space.dotcat.popularmovies.utils.bottomNavigation.BottomNavigationUtils;

public class MoviesActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tb_app_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.btv_navigation_view)
    BottomNavigationView mBottomNavigationView;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, MoviesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mi_settings) {
            resetFragmentError();

            startSettingsActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        resetFragmentError();

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

    private void startSettingsActivity() {
        SettingsActivity.start(this);
    }

    private void resetFragmentError() {
        BaseFragment baseFragment = (BaseFragment) mFragmentManager.findFragmentById(R.id.fl_fragment_container);

        baseFragment.resetError();
    }

    private void setupNavigationView() {
        BottomNavigationUtils.removeShiftMode(mBottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

}
