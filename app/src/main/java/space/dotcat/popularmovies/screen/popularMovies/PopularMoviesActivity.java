package space.dotcat.popularmovies.screen.popularMovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import space.dotcat.popularmovies.AppDelegate;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.di.presentationLayer.PopularMoviesActivityModule;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.screen.base.BaseActivity;
import space.dotcat.popularmovies.screen.popularMovieDetails.MovieDetailsActivity;

public class PopularMoviesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        PopularMoviesAdapter.OnMovieClickListener{

    @Inject
    PopularMoviesViewModelFactory mFactory;

    @Inject
    PopularMoviesAdapter mMoviesAdapter;

    @BindView(R.id.rv_movies)
    RecyclerView mMovies;

    @BindView(R.id.srl_update_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_empty_message)
    TextView mEmptyErrorMessage;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.tb_app_toolbar)
    Toolbar mToolbar;

    private PopularMoviesViewModel mPopularMoviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark));

        mPopularMoviesViewModel = ViewModelProviders.of(this, mFactory)
                .get(PopularMoviesViewModel.class);

        mMovies.setLayoutManager(new GridLayoutManager(this, getNumberOfColumns()));
        mMovies.setHasFixedSize(true);
        mMovies.setAdapter(mMoviesAdapter);
    }

    @Override
    protected void setupActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.app_name));

        setToolbar(mToolbar);

        super.setupActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPopularMoviesViewModel.observeLoadingIndicator().observe(this, isLoading-> {
            if(isLoading) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        mPopularMoviesViewModel.observeErrors().observe(this, error -> {
            if(!error.isExist()) {
                return;
            }

            Throwable throwable = error.getThrowable();

            super.showError(throwable, mSwipeRefreshLayout, errorHandler ->
                    mPopularMoviesViewModel.resetError());
        });

        mPopularMoviesViewModel.getPopularMovies().observe(this, movies -> {
            if (movies.isEmpty()) {
                showEmptyError();
            } else {
                showMovies(movies);
            }
        });
    }

    private void showMovies(List<Movie> movies) {
        mEmptyErrorMessage.setVisibility(View.INVISIBLE);
        mMovies.setVisibility(View.VISIBLE);

        if(getSnackbar() != null) {
            if(getSnackbar().isShown()){
                getSnackbar().dismiss();
            }
        }

        mMoviesAdapter.updateMovies(movies);
    }

    private void showEmptyError() {
        mMovies.setVisibility(View.INVISIBLE);
        mEmptyErrorMessage.setVisibility(View.VISIBLE);
        mEmptyErrorMessage.setText(getResources().getString(R.string.empty_error_message));
    }

    @Override
    protected void initDependencyGraph() {
        AppDelegate.getInstance()
                .getAppLayerComponent()
                .plusPopularMoviesComponent(new PopularMoviesActivityModule(getScreenSize(), this))
                .inject(this);
    }


    private ImageSize getScreenSize() {
        int number_of_columns = getNumberOfColumns();
        int number_of_rows = getNumberOfRows();

        TypedValue tv = new TypedValue();

        int actionBarHeight = getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)
                ? TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
                : 0;

        int imageWidth = getResources().getDisplayMetrics().widthPixels / number_of_columns;
        int imageHeight = (getResources().getDisplayMetrics().heightPixels - actionBarHeight) / number_of_rows;

        return new ImageSize(imageWidth, imageHeight);
    }

    private int getNumberOfColumns() {
        return getResources().getInteger(R.integer.number_of_columns);
    }

    private int getNumberOfRows() {
        return getResources().getInteger(R.integer.number_of_rows);
    }

    @Override
    public void onRefresh() {
        mPopularMoviesViewModel.reloadMovies();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMovieClick(int movieId) {
        MovieDetailsActivity.start(this, movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.movie_list_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mi_sort_by_rating: {
                mPopularMoviesViewModel.sortMoviesByRating();

                return true;
            }

            case R.id.mi_sort_by_popularity: {
                mPopularMoviesViewModel.sortMoviesByPopularity();

                return true;
            }

            case R.id.mi_show_only_favorite: {
                mPopularMoviesViewModel.getFavoriteMovies();

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
