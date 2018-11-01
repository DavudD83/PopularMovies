package space.dotcat.popularmovies.viewModels;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.FlexInterval;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.screen.movies.fragments.BaseMoviesInternetViewModel;
import space.dotcat.popularmovies.screen.movies.fragments.popularMovies.PopularMoviesViewModel;
import space.dotcat.popularmovies.utils.updatePeriodCalculator.FlexIntervalCalculator;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MoviesViewModelWithInternetTest extends BaseViewModelTest<PopularMoviesViewModel> {

    private static final Long ONE_DAY_UPDATE_PERIOD = 1L;

    private static final Long THREE_DAY_UPDATE_PERIOD = 3L;

    private static final Long SEVEN_DAY_UPDATE_PERIOD = 7L;

    @Mock
    Scheduler mScheduler;

    @Mock
    KeyValueRepository mKeyValueRepository;

    @Mock
    FlexIntervalCalculator mFlexIntervalCalculator;

    private final Throwable ERROR = new Throwable();

    private final List<Movie> MOVIES = createSortedMovieList();

    private final List<Movie> EMPTY_MOVIES = new ArrayList<>();

    private final List<Movie> SORTED_MOVIES = createSortedMovieList();

    private final Flowable<List<Movie>> FLOWABLE_MOVIES = Flowable.just(MOVIES);

    private final Flowable<List<Movie>> EMPTY_FLOWABLE = Flowable.just(EMPTY_MOVIES);

    private final Flowable<List<Movie>> FLOWABLE_MOVIES_SORTED = Flowable.just(SORTED_MOVIES);

    private final Flowable<List<Movie>> FLOWABLE_ERROR = Flowable.error(ERROR);

    @Override
    protected PopularMoviesViewModel createViewModelForTesting() {
        return new PopularMoviesViewModel(mMoviesRepository, mScheduler, mKeyValueRepository,
                mFlexIntervalCalculator);
    }

    @Test
    public void testViewModelCreated() {
        assertNotNull(mViewModel);
    }

    @Test
    public void testGetMoviesWhenMoviesInDb() {
        when(mMoviesRepository.getMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_MOVIES);

        mViewModel.getMovies().observeForever(mMoviesObserver);

        verify(mLoadingObserver).onChanged(true);

        verify(mMoviesRepository).getMoviesWithFlag(Movie.FLAG_POPULAR);

        verify(mLoadingObserver).onChanged(false);

        verify(mMoviesObserver).onChanged(MOVIES);
    }

    @Test
    public void testGetMoviesWhenNoMoviesInDb() {
        when(mMoviesRepository.getMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(EMPTY_FLOWABLE);

        when(mMoviesRepository.reloadMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_MOVIES);

        mViewModel.getMovies().observeForever(mMoviesObserver);

        verify(mMoviesRepository).reloadMoviesWithFlag(Movie.FLAG_POPULAR);

        verifyNoMoreInteractions(mMoviesObserver);
    }

    @Test
    public void testGetMoviesWhenServerReturnedZeroMovies() {
        when(mMoviesRepository.getMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(EMPTY_FLOWABLE);

        when(mMoviesRepository.reloadMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(EMPTY_FLOWABLE);

        mViewModel.getMovies().observeForever(mMoviesObserver);

        verify(mMoviesRepository).reloadMoviesWithFlag(Movie.FLAG_POPULAR);
        verify(mMoviesObserver).onChanged(EMPTY_MOVIES);
    }

    @Test
    public void testGetMoviesFromDbWithError() {
        when(mMoviesRepository.getMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_ERROR);

        mViewModel.getMovies().observeForever(mMoviesObserver);

        verify(mMoviesRepository).getMoviesWithFlag(Movie.FLAG_POPULAR);

        verify(mErrorObserver).onChanged(argThat(error-> error.getErrorCode() == Error.UNKNOWN_ERROR));
    }

    @Test
    public void testReloadMoviesSuccessfully() {
        when(mMoviesRepository.reloadMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_MOVIES);

        mViewModel.reloadMovies();

        verify(mMoviesRepository).reloadMoviesWithFlag(Movie.FLAG_POPULAR);

        verifyNoMoreInteractions(mErrorObserver);
    }

    @Test
    public void testReloadMoviesWithError() {
        when(mMoviesRepository.reloadMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_ERROR);

        mViewModel.reloadMovies();

        verify(mMoviesRepository).reloadMoviesWithFlag(Movie.FLAG_POPULAR);

        verify(mErrorObserver).onChanged(argThat(error-> error.getErrorCode() ==
                BaseMoviesInternetViewModel.INTERNET_CONNECTION_PROBLEM));
    }

    @Test
    public void testGetMoviesSortedByPopularity() {
        when(mMoviesRepository.getMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_MOVIES);

        when(mMoviesRepository.getMoviesWithFlagSortedByPopularity(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_MOVIES_SORTED);

        mViewModel.getMovies().observeForever(mMoviesObserver);

        mViewModel.getMoviesSortedByPopularity();

        verify(mMoviesRepository).getMoviesWithFlagSortedByPopularity(Movie.FLAG_POPULAR);

        verify(mMoviesObserver, times(2)).onChanged(argThat(movies->
                movies.get(0).getPopularity() == 20.0f &&
                movies.get(1).getPopularity() == 12.0f &&
                movies.get(2).getPopularity() == 3.0f));
    }

    @Test
    public void testGetMoviesSortedByRating() {
        when(mMoviesRepository.getMoviesWithFlag(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_MOVIES);

        when(mMoviesRepository.getMoviesWithFlagSortedByRating(Movie.FLAG_POPULAR)).thenReturn(FLOWABLE_MOVIES_SORTED);

        mViewModel.getMovies().observeForever(mMoviesObserver);

        mViewModel.getMoviesSortedByRating();

        verify(mMoviesRepository).getMoviesWithFlagSortedByRating(Movie.FLAG_POPULAR);

        verify(mMoviesObserver, times(2)).onChanged(argThat(movies->
                movies.get(0).getVote_average() == 20.0f &&
                movies.get(1).getVote_average() == 12.0f &&
                movies.get(2).getVote_average() == 3.0f));
    }

    @Test
    public void testStartSchedulingJobWithThreeDayPref() {
        when(mKeyValueRepository.getPeriodOfUpdatingPopularMovies()).thenReturn(THREE_DAY_UPDATE_PERIOD);

        FlexInterval flexInterval = new FlexInterval(2, TimeUnit.DAYS);

        when(mFlexIntervalCalculator.calculateFlexInterval(THREE_DAY_UPDATE_PERIOD)).thenReturn(flexInterval);

        mViewModel.startSchedulingJob();

        verify(mScheduler).startDeletingUnflagedMovies();
        verify(mScheduler).startUpdatingPopularMovies(THREE_DAY_UPDATE_PERIOD, 2, TimeUnit.DAYS);
    }

    @Test
    public void testStartSchedulingJobWithOneDayPref() {
        when(mKeyValueRepository.getPeriodOfUpdatingPopularMovies()).thenReturn(ONE_DAY_UPDATE_PERIOD);

        FlexInterval flexInterval = new FlexInterval(20, TimeUnit.HOURS);

        when(mFlexIntervalCalculator.calculateFlexInterval(ONE_DAY_UPDATE_PERIOD)).thenReturn(flexInterval);

        mViewModel.startSchedulingJob();

        verify(mScheduler).startDeletingUnflagedMovies();
        verify(mScheduler).startUpdatingPopularMovies(ONE_DAY_UPDATE_PERIOD, 20, TimeUnit.HOURS);
    }

    @Test
    public void testStartSchedulingJobWithSevenDayPref() {
        when(mKeyValueRepository.getPeriodOfUpdatingPopularMovies()).thenReturn(SEVEN_DAY_UPDATE_PERIOD);

        FlexInterval flexInterval = new FlexInterval(6, TimeUnit.DAYS);

        when(mFlexIntervalCalculator.calculateFlexInterval(SEVEN_DAY_UPDATE_PERIOD)).thenReturn(flexInterval);

        mViewModel.startSchedulingJob();

        verify(mScheduler).startDeletingUnflagedMovies();
        verify(mScheduler).startUpdatingPopularMovies(SEVEN_DAY_UPDATE_PERIOD, 6, TimeUnit.DAYS);
    }

    private List<Movie> createSortedMovieList() {
        Movie popularMovie = new Movie();
        popularMovie.setPopularity(20.0f);
        popularMovie.setVote_average(20.0f);
        popularMovie.setPopular(true);

        Movie popularMovie1 = new Movie();
        popularMovie1.setPopularity(12.0f);
        popularMovie1.setVote_average(12.0f);
        popularMovie.setPopular(true);

        Movie popularMovie2 = new Movie();
        popularMovie2.setPopularity(3.0f);
        popularMovie2.setVote_average(3.0f);
        popularMovie.setPopular(true);

        return Arrays.asList(popularMovie, popularMovie1, popularMovie2);
    }
}
