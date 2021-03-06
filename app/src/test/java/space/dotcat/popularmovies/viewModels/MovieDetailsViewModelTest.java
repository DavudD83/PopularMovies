package space.dotcat.popularmovies.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.MovieExtraInfo;
import space.dotcat.popularmovies.model.Video;
import space.dotcat.popularmovies.screen.movieDetails.fragments.MovieDetailsViewModel;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MovieDetailsViewModelTest extends BaseViewModelTest<MovieDetailsViewModel> {

    private static final int TEST_MOVIE_ID = 1;

    @Mock
    private Observer<Movie> mMovieObserver;

    @Mock
    private Observer<MovieExtraInfo> mMovieExtraInfoObserver;

    private final Movie MOVIE = new Movie();

    private final MovieExtraInfo MOVIE_EXTRA_INFO = new MovieExtraInfo(new Video(), new ArrayList<>());

    private final Flowable<MovieExtraInfo> FLOWABL_INFO = Flowable.just(MOVIE_EXTRA_INFO);

    private final Throwable ERROR = new Throwable();

    private final Flowable<MovieExtraInfo> FLOWABLE_ERROR_WITH_DATA =
            Flowable.mergeDelayError(Flowable.just(MOVIE_EXTRA_INFO), Flowable.error(ERROR));

    private final Completable COMPLETABLE = Completable.complete();

    private LiveData mMovie;

    @Override
    protected MovieDetailsViewModel createViewModelForTesting() {
        return new MovieDetailsViewModel(TEST_MOVIE_ID, mMoviesRepository);
    }

    @Override
    public void init() {
        super.init();

        mMovie = createLiveDataWithMovie();
    }

    @Test
    public void testCreateViewModel() {
        assertNotNull(mViewModel);
    }

    @Test
    public void testGetMovieById() {
        when(mMoviesRepository.getMovieById(TEST_MOVIE_ID)).thenReturn(mMovie);

        mViewModel.getMovie().observeForever(mMovieObserver);

        verify(mMoviesRepository).getMovieById(TEST_MOVIE_ID);

        verify(mMovieObserver).onChanged(MOVIE);

        verifyNoMoreInteractions(mErrorObserver);
    }

    @Test
    public void testLoadTrailersAndReviews() {
        when(mMoviesRepository.getTrailersAndReviews(TEST_MOVIE_ID)).thenReturn(FLOWABL_INFO);

        mViewModel.getTrailerAndReviews().observeForever(mMovieExtraInfoObserver);

        verify(mMoviesRepository).getTrailersAndReviews(TEST_MOVIE_ID);

        verify(mMovieExtraInfoObserver).onChanged(MOVIE_EXTRA_INFO);

        verifyNoMoreInteractions(mErrorObserver);
    }

    @Test
    public void testLoadTrailersAndReviewsWithErrorAndData() {
        when(mMoviesRepository.getTrailersAndReviews(TEST_MOVIE_ID)).thenReturn(FLOWABLE_ERROR_WITH_DATA);

        mViewModel.getTrailerAndReviews().observeForever(mMovieExtraInfoObserver);

        verify(mMoviesRepository).getTrailersAndReviews(TEST_MOVIE_ID);

        verify(mMovieExtraInfoObserver).onChanged(MOVIE_EXTRA_INFO);
        verify(mErrorObserver).onChanged(any(Error.class));
    }

    @Test
    public void testMarkMovieAsFavourite() {
        when(mMoviesRepository.getMovieById(TEST_MOVIE_ID)).thenReturn(mMovie);

        when(mMoviesRepository.updateMovie(any(Movie.class))).thenReturn(COMPLETABLE);

        mViewModel.getMovie().observeForever(mMovieObserver);

        mViewModel.updateMovie(true);

        verify(mMoviesRepository).updateMovie(argThat(Movie::isFavorite));
    }

    private MutableLiveData createLiveDataWithMovie() {
        MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();

        movieLiveData.setValue(MOVIE);

        return movieLiveData;
    }
}
