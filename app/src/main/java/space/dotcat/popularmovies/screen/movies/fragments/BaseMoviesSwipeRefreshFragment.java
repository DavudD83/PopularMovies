package space.dotcat.popularmovies.screen.movies.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Error;

public abstract class BaseMoviesSwipeRefreshFragment<VM extends BaseMoviesInternetViewModel> extends BaseMoviesFragment<VM>
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.srl_update_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setupSwipeRefreshLayout();

        return view;
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void handleError(Error error) {
        super.handleError(error);

        if (error.getErrorCode() == BaseMoviesInternetViewModel.INTERNET_CONNECTION_PROBLEM) {
            super.showError(getString(R.string.error_loading_movies), v-> {
                mViewModel.reloadMovies();

                mViewModel.resetError();
            });
        }
    }

    @Override
    public void onRefresh() {
        mViewModel.reloadMovies();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
