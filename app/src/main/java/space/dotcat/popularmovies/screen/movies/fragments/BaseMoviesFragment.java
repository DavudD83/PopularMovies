package space.dotcat.popularmovies.screen.movies.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.screen.base.BaseFragment;
import space.dotcat.popularmovies.screen.movieDetails.activity.MovieDetailsActivity;

public abstract class BaseMoviesFragment<VM extends BaseMoviesViewModel> extends BaseFragment<VM>
        implements MoviesAdapter.OnMovieClickListener {

    @Inject
    public ViewModelProvider.Factory mFactory;

    @Inject
    MoviesAdapter mMoviesAdapter;

    @BindView(R.id.rv_movies)
    RecyclerView mMovies;

    @BindView(R.id.tv_empty_message)
    public TextView mEmptyErrorMessage;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.cl_wrapper_layout)
    ConstraintLayout mWrapperLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        setHasProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        ButterKnife.bind(this, view);

        setupLoadingIndicator(mProgressBar);

        setupRecycler();

        setupErrorMessage();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.observeErrors().observe(this, error-> {
            if (!error.isExist()) {
                return;
            }

            handleError(error);
        });

        mViewModel.getMovies().observe(this, movies -> {
            if (movies.isEmpty()) {
                showEmptyMoviesMessage();
            } else {
                showMovies(movies);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.mi_sort_by_rating: {
                mViewModel.getMoviesSortedByRating();

                return true;
            }

            case R.id.mi_sort_by_popularity: {
                mViewModel.getMoviesSortedByPopularity();

                return true;
            }

            case R.id.mi_settings: {

            }

            default: {
                return false;
            }
        }
    }

    @Override
    protected View getContainerForSnackbar() {
        return mWrapperLayout;
    }

    @Override
    public void onMovieClick(int movieId) {
        MovieDetailsActivity.start(getActivity(), movieId);
    }

    protected abstract void setupErrorMessage();

    protected void showMovies(List<Movie> movies) {
        mEmptyErrorMessage.setVisibility(View.INVISIBLE);
        mMovies.setVisibility(View.VISIBLE);

        hideError();

        mMoviesAdapter.changeData(movies);
    }

    protected void handleError(Error error) {
        Throwable throwable = error.getThrowable();

        if (error.getErrorCode() == Error.UNKNOWN_ERROR) {
            super.showError(throwable.getMessage(), v-> mViewModel.resetError());
        }
    }

    public ImageSize getScreenSize() {
        int number_of_columns = getNumberOfColumns();
        int number_of_rows = getNumberOfRows();

        TypedValue tv = new TypedValue();

        int actionBarHeight = getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)
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

    private void hideError() {
        Snackbar snackbar = getSnackbar();

        if (snackbar != null) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }

        mViewModel.resetError();
    }

    private void showEmptyMoviesMessage() {
        mMovies.setVisibility(View.INVISIBLE);
        mEmptyErrorMessage.setVisibility(View.VISIBLE);
    }

    private void setupRecycler() {
        mMovies.setLayoutManager(new GridLayoutManager(getContext(), getNumberOfColumns()));

        mMovies.setAdapter(mMoviesAdapter);

        mMovies.setHasFixedSize(true);
    }
}
