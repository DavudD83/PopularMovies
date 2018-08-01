package space.dotcat.popularmovies.screen.popularMovieDetails;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import space.dotcat.popularmovies.AppDelegate;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.di.presentationLayer.MovieDetailsActivityModule;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.screen.base.BaseActivity;
import space.dotcat.popularmovies.widget.CheckableFloatingButton;

public class MovieDetailsActivity extends BaseActivity {

    private static final String MOVIE_ID_KEY = "MOVIE_ID_KEY";

    @Inject
    MovieDetailsViewModelFactory mDetailsViewModelFactory;

    @Inject
    ReviewsAdapter mReviewsAdapter;

    @BindView(R.id.iv_movie_details_image)
    ImageView mMovieImage;

    @BindView(R.id.tb_movie_details_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ctl_collapsing_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.tv_movie_rating)
    TextView mMovieRating;

    @BindView(R.id.tv_movie_overview)
    TextView mMovieOverview;

    @BindView(R.id.tv_movie_release_day)
    TextView mMovieReleaseDay;

    @BindView(R.id.iv_play_trailer)
    ImageView mPlayTrailerImage;

    @BindView(R.id.cl_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.rv_reviews)
    RecyclerView mReviews;

    @BindView(R.id.pb_loading)
    ProgressBar mProgressBar;

    @BindView(R.id.fb_favorite_film)
    CheckableFloatingButton mFavoriteButton;

    @BindView(R.id.tv_reviews_empty_message)
    TextView mEmptyReviewsError;

    private MovieDetailsViewModel mDetailsViewModel;

    public static void start(@NonNull Activity activity, int movieId) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);

        intent.putExtra(MOVIE_ID_KEY, movieId);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mReviews.setLayoutManager(new LinearLayoutManager(this));
        mReviews.setAdapter(mReviewsAdapter);

        mDetailsViewModel = ViewModelProviders.of(this, mDetailsViewModelFactory).
                get(MovieDetailsViewModel.class);

        mDetailsViewModel.observeLoadingIndicator().observe(this, isLoading -> {
            if (isLoading) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        mDetailsViewModel.observeErrors().observe(this, error -> {
            if (!error.isExist()) {
                return;
            }

            Throwable throwable = error.getThrowable();

            if (error.getErrorCode() == MovieDetailsViewModel.LOADING_REVIEWS_ERROR) {
                super.showError(new Throwable(getString(R.string.error_loading_reviews)), mCoordinatorLayout,
                        v -> {
                            mDetailsViewModel.loadTrailersAndReviews();
                            mDetailsViewModel.resetError();
                        });

                return;
            }

            super.showError(throwable, mCoordinatorLayout, v -> mDetailsViewModel.resetError());
        });

        mDetailsViewModel.getMovie().observe(this, this::showMovie);

        mDetailsViewModel.getTrailerAndReviews().observe(this, movieExtraInfo -> {
            if (movieExtraInfo == null) {
                return;
            }

            List<Review> reviewList = movieExtraInfo.getReviewList();

            //TODO

            if (reviewList != null && reviewList.isEmpty()) {
                showEmptyReviewsError();

            } else if (reviewList != null) {
                showReviews(reviewList);
            }
        });
    }

    private void showEmptyReviewsError() {
        mReviews.setVisibility(View.INVISIBLE);
        mEmptyReviewsError.setVisibility(View.VISIBLE);
    }

    private void showReviews(List<Review> reviews) {
        mReviews.setVisibility(View.VISIBLE);

        mEmptyReviewsError.setVisibility(View.INVISIBLE);

        mReviewsAdapter.updateReviews(reviews);
    }

    @Override
    protected void setupActionBar() {
        setToolbar(mToolbar);

        super.setupActionBar();
    }

    private void showMovie(Movie movie) {
        mCollapsingToolbarLayout.setTitle(movie.getTitle());

        String url = "https://image.tmdb.org/t/p/w780/" + movie.getPoster_path();

        Picasso.get()
                .load(url)
                .noFade()
                .into(mMovieImage);

        mMovieRating.setText(String.valueOf(movie.getVote_average()));

        mMovieReleaseDay.setText(movie.getReleaseDate());

        mMovieOverview.setText(movie.getOverview());

        mFavoriteButton.setChecked(movie.isFavorite());
    }

    @Override
    protected void initDependencyGraph() {
        int movie_id = getIntent().getIntExtra(MOVIE_ID_KEY, 0);

        AppDelegate.getInstance()
                .getAppLayerComponent()
                .plusMovieDetailsComponent(new MovieDetailsActivityModule(movie_id))
                .inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.movie_details_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                onBackPressed();

                return true;
            }

            case R.id.mi_share_movie: {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                Movie movie = mDetailsViewModel.getMovie().getValue();

                if (movie == null) {
                    Toast.makeText(this, R.string.share_error, Toast.LENGTH_LONG).show();

                    return true;
                }

                shareIntent.putExtra(Intent.EXTRA_TEXT, "Take a look at this film " + movie.getTitle()
                        + ".Average rating " + movie.getVote_average());

                shareIntent.setType("text/plain");

                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(shareIntent);
                }

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.iv_play_trailer)
    public void watchTrailer(View view) {
        MovieExtraInfo movieExtraInfo = mDetailsViewModel.getTrailerAndReviews().getValue();

        if (movieExtraInfo == null) {
            Toast.makeText(this, R.string.trailer_not_loaded_error, Toast.LENGTH_LONG).show();

            return;
        }

        String url = "https://www.youtube.com/watch?v=" + movieExtraInfo.getTrailer().getKey();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.fb_favorite_film)
    public void favorite(View view) {
        CheckableFloatingButton checkFloatingButton = (CheckableFloatingButton) view;

        boolean isFavorite = checkFloatingButton.isChecked();

        mDetailsViewModel.updateMovie(isFavorite);
    }
}
