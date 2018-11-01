package space.dotcat.popularmovies.screen.movieDetails.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.base.BaseActivity;
import space.dotcat.popularmovies.screen.movieDetails.OnReloadFinished;
import space.dotcat.popularmovies.screen.movieDetails.Refreshable;
import space.dotcat.popularmovies.screen.movieDetails.fragments.MovieDetailsFragment;
import space.dotcat.popularmovies.widget.CheckableFloatingButton;

public class MovieDetailsActivity extends BaseActivity implements
        MovieDetailsFragment.OnChangeToolbarTitle,
        MovieDetailsFragment.PosterViewHolder,
        MovieDetailsFragment.OnChangeButtonFavoriteState,
        SwipeRefreshLayout.OnRefreshListener,
        OnReloadFinished {

    private static final String EXTRA_MOVIE_ID_KEY = "EXTRA_MOVIE_ID_KEY";

    @BindView(R.id.iv_movie_details_image)
    public ImageView mMovieImage;

    @BindView(R.id.tb_movie_details_toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.ctl_collapsing_layout)
    public CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.iv_play_trailer)
    public ImageView mPlayTrailerImage;

    @BindView(R.id.fb_favorite_film)
    public CheckableFloatingButton mFavoriteButton;

    @BindView(R.id.srl_refresh_layout)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    public static void start(@NonNull Activity context, int movieId) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);

        intent.putExtra(EXTRA_MOVIE_ID_KEY, movieId);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        int movieId = intent.getIntExtra(EXTRA_MOVIE_ID_KEY, 0);

        if (savedInstanceState == null) {
            addFragment(R.id.fl_container, MovieDetailsFragment.create(movieId));
        }

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onSetupActionBar() {
        setToolbar(mToolbar);

        super.onSetupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                onBackPressed();

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.iv_play_trailer)
    public void watchTrailer(View view) {
        MovieDetailsFragment fragment = (MovieDetailsFragment) mFragmentManager
                .findFragmentById(R.id.fl_container);

        fragment.watchTrailer();
    }

    @OnClick(R.id.fb_favorite_film)
    public void favorite(View view) {
        CheckableFloatingButton checkFloatingButton = (CheckableFloatingButton) view;

        boolean isFavorite = checkFloatingButton.isChecked();

        MovieDetailsFragment fragment = (MovieDetailsFragment) mFragmentManager
                .findFragmentById(R.id.fl_container);

        fragment.updateMovie(isFavorite);
    }

    @Override
    public void changeToolbarTitle(String title) {
        mCollapsingToolbarLayout.setTitle(title);
    }

    @Override
    public ImageView getMoviePosterView() {
        return mMovieImage;
    }

    @Override
    public void changeFavoriteState(boolean state) {
        mFavoriteButton.setChecked(state);
    }

    @Override
    public void onRefresh() {
        Refreshable refreshable = (Refreshable) mFragmentManager.findFragmentById(R.id.fl_container);

        refreshable.reloadData();
    }

    @Override
    public void reloadFinished() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
