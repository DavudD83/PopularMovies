package space.dotcat.popularmovies.screen.movieDetails.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.dotcat.popularmovies.BuildConfig;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.screen.base.BaseFragment;
import space.dotcat.popularmovies.screen.movieDetails.OnReloadFinished;
import space.dotcat.popularmovies.screen.movieDetails.Refreshable;
import space.dotcat.popularmovies.utils.image.ImageLoader;


public class MovieDetailsFragment extends BaseFragment<MovieDetailsViewModel> implements Refreshable {

    public static final String EXTRA_MOVIE_ID_KEY = "EXTRA_MOVIE_ID_KEY";

    @Inject
    MovieDetailsViewModelFactory mPopularMovieDetailsViewModelFactory;

    @Inject
    ReviewsAdapter mReviewsAdapter;

    @Inject
    ImageLoader mImageLoader;

//    @BindView(R.id.srl_refresh_layout)
//    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_movie_rating)
    TextView mMovieRating;

    @BindView(R.id.tv_movie_overview)
    TextView mMovieOverview;

    @BindView(R.id.tv_movie_release_day)
    TextView mMovieReleaseDay;

    @BindView(R.id.cl_constraint_layout)
    ConstraintLayout mWrapperLayout;

    @BindView(R.id.rv_reviews)
    RecyclerView mReviews;

    @BindView(R.id.pb_loading)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_reviews_empty_message)
    TextView mEmptyReviewsError;

    private OnChangeToolbarTitle mOnChangeToolbarTitle;

    private PosterViewHolder mPosterViewHolder;

    private OnChangeButtonFavoriteState mOnChangeButtonFavoriteState;

    private OnReloadFinished mOnReloadFinished;

    private Toast mToast = null;

    public static MovieDetailsFragment create(int movieId) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MOVIE_ID_KEY, movieId);

        MovieDetailsFragment popularMovieDetailsFragment = new MovieDetailsFragment();
        popularMovieDetailsFragment.setArguments(bundle);

        return popularMovieDetailsFragment;
    }

    public MovieDetailsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnChangeToolbarTitle = (OnChangeToolbarTitle) context;

            mPosterViewHolder = (PosterViewHolder) context;

            mOnChangeButtonFavoriteState = (OnChangeButtonFavoriteState) context;

            mOnReloadFinished = (OnReloadFinished) context;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Host activity has to implement appropriate interface. " + ex.getMessage());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        setHasProgressIndicator();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, view);

        setupReviewsRecycler();

        setupLoadingIndicator(mProgressBar);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.observeErrors().observe(this, error-> {
            if (!error.isExist()) {
                return;
            }

            if (error.getErrorCode() == MovieDetailsViewModel.LOADING_ERROR) {
                showToast(getString(R.string.error_loading_reviews_trailer), Toast.LENGTH_SHORT);

                error.resetError();

                return;
            }

            super.showError(error.getThrowable().getMessage(), v-> mViewModel.resetError());
        });

        mViewModel.getMovie().observe(this, this::showMovie);

        mViewModel.getTrailerAndReviews().observe(this, result -> {
            if (result.getReviewList().size() > 0) {
                showReviews(result.getReviewList());
            } else {
                showEmptyReviewsError();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mi_share_movie: {
                Movie movie = mViewModel.getMovie().getValue();

                if (movie == null) {
                    Toast.makeText(getContext(), "Movie has not been loaded yet. " +
                            "So you can not share it yet.", Toast.LENGTH_SHORT).show();

                    return false;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Take a look at this film " + movie.getTitle()
                        + ". Average rating is " + movie.getVote_average());

                Intent sharedIntent = Intent.createChooser(intent, "Share movie that you have chosen");

                if (sharedIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(sharedIntent);
                }

                return true;
            }

            default: {
                return false;
            }
        }
    }

    @Override
    protected MovieDetailsViewModel createViewModel() {
        return ViewModelProviders.of(this, mPopularMovieDetailsViewModelFactory)
                .get(MovieDetailsViewModel.class);
    }

    @Override
    protected View getContainerForSnackbar() {
        return mWrapperLayout;
    }

//    @Override
//    public void onRefresh() {
//        mViewModel.loadTrailersAndReviews();
//        mSwipeRefreshLayout.setRefreshing(false);
//    }


    public void updateMovie(boolean isFavorite) {
        mViewModel.updateMovie(isFavorite);
    }

    public void watchTrailer() {
        MovieExtraInfo movieExtraInfo = mViewModel.getTrailerAndReviews().getValue();

        if (movieExtraInfo == null) {
            showToast(getString(R.string.trailer_not_loaded_error), Toast.LENGTH_SHORT);

            return;
        }

        if (movieExtraInfo.getTrailer() != null) {
            String path = BuildConfig.BASE_YOUTUBE_URL + movieExtraInfo.getTrailer().getKey();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        } else {
            showToast(getString(R.string.empty_trailer), Toast.LENGTH_SHORT);
        }
    }

    private void showReviews(List<Review> reviews) {
        mEmptyReviewsError.setVisibility(View.INVISIBLE);
        mReviews.setVisibility(View.VISIBLE);

        mReviewsAdapter.changeData(reviews);
    }

    private void showEmptyReviewsError() {
        mReviews.setVisibility(View.INVISIBLE);
        mEmptyReviewsError.setVisibility(View.VISIBLE);
    }

    private void showMovie(Movie movie) {
        mOnChangeToolbarTitle.changeToolbarTitle(movie.getTitle());

        ImageView moviePoster = mPosterViewHolder.getMoviePosterView();

        mImageLoader.loadLargePoster(moviePoster, movie.getPoster_path());

        mMovieRating.setText(String.valueOf(movie.getVote_average()));

        mMovieReleaseDay.setText(movie.getReleaseDate());

        mMovieOverview.setText(movie.getOverview());

        mOnChangeButtonFavoriteState.changeFavoriteState(movie.isFavorite());
    }

    private void setupReviewsRecycler() {
        mReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviews.setAdapter(mReviewsAdapter);
    }

    private void showToast(String message, int lengthLong) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getContext(), message, lengthLong);

        mToast.show();
    }

    @Override
    public void reloadData() {
        mViewModel.loadTrailersAndReviews();

        mOnReloadFinished.reloadFinished();
    }

    public interface OnChangeToolbarTitle {
        void changeToolbarTitle(String title);
    }

    public interface PosterViewHolder {
        ImageView getMoviePosterView();
    }

    public interface OnChangeButtonFavoriteState {
        void changeFavoriteState(boolean state);
    }
}
