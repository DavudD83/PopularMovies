package space.dotcat.popularmovies.screen.popularMovieDetails.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.base.BaseActivity;
import space.dotcat.popularmovies.screen.popularMovieDetails.fragments.PopularMovieDetailsFragment;
import space.dotcat.popularmovies.widget.CheckableFloatingButton;

public class MovieDetailsActivity extends BaseActivity implements PopularMovieDetailsFragment.OnChangeToolbarTitle,
        PopularMovieDetailsFragment.PosterViewHolder, PopularMovieDetailsFragment.OnChangeButtonFavoriteState {

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
            addFragment(R.id.fl_container, PopularMovieDetailsFragment.create(movieId));
        }
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
        PopularMovieDetailsFragment fragment = (PopularMovieDetailsFragment) mFragmentManager
                .findFragmentById(R.id.fl_container);

        fragment.watchTrailer();
    }

    @OnClick(R.id.fb_favorite_film)
    public void favorite(View view) {
        CheckableFloatingButton checkFloatingButton = (CheckableFloatingButton) view;

        boolean isFavorite = checkFloatingButton.isChecked();

        PopularMovieDetailsFragment fragment = (PopularMovieDetailsFragment) mFragmentManager
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
}
