package space.dotcat.popularmovies.viewModels;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.screen.popularMovies.PopularMoviesViewModel;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MoviesViewModelTest extends BaseViewModelTest<PopularMoviesViewModel> {

    private final Throwable ERROR = new Throwable();

    private final List<Movie> MOVIES = createMovieList();

    private final List<Movie> SORTED_MOVIES = createMovieList();

    private final Flowable<List<Movie>> FLOWABLE_MOVIES = Flowable.just(MOVIES);

    private final Flowable<List<Movie>> FLOWABLE_MOVIES_SORTED = Flowable.just(SORTED_MOVIES);

    private final Flowable<List<Movie>> FLOWABLE_ERROR = Flowable.error(ERROR);

    @Override
    protected PopularMoviesViewModel createViewModelForTesting() {
        return new PopularMoviesViewModel(mMoviesRepository);
    }

    @Test
    public void testViewModelCreated() {
        assertNotNull(mViewModel);
    }

    @Test
    public void testGetMoviesSuccessfully() {
        when(mMoviesRepository.getPopularMovies()).thenReturn(FLOWABLE_MOVIES);

        mViewModel.getPopularMovies().observeForever(mMoviesObserver);

        verify(mMoviesRepository).getPopularMovies();

        verify(mLoadingObserver).onChanged(true);
        verify(mLoadingObserver).onChanged(false);

        verify(mMoviesObserver).onChanged(MOVIES);

        verifyNoMoreInteractions(mErrorObserver);
    }

    @Test
    public void testGetMoviesWithError() {
        Flowable<List<Movie>> moviesWithError = Flowable.mergeDelayError(Flowable.just(MOVIES),
                Flowable.error(ERROR));

        when(mMoviesRepository.getPopularMovies()).thenReturn(moviesWithError);

        mViewModel.getPopularMovies().observeForever(mMoviesObserver);

        mViewModel.getPopularMovies();

        verify(mLoadingObserver).onChanged(true);

        verify(mLoadingObserver, times(2)).onChanged(false);

        verify(mMoviesObserver).onChanged(MOVIES);

        verify(mErrorObserver).onChanged(any(Error.class));
    }

    @Test
    public void testSortMoviesByRating() {
        when(mMoviesRepository.getPopularMoviesSortedByRating()).thenReturn(FLOWABLE_MOVIES_SORTED);
        when(mMoviesRepository.getPopularMovies()).thenReturn(FLOWABLE_MOVIES);

        mViewModel.getPopularMovies().observeForever(mMoviesObserver);

        verify(mMoviesObserver).onChanged(MOVIES);

        mViewModel.sortMoviesByRating();

        verify(mMoviesObserver).onChanged(SORTED_MOVIES);
    }

    @Test
    public void testSortMoviesByRatingWithError() {
        when(mMoviesRepository.getPopularMovies()).thenReturn(FLOWABLE_MOVIES);
        when(mMoviesRepository.getPopularMoviesSortedByRating()).thenReturn(FLOWABLE_ERROR);

        mViewModel.getPopularMovies().observeForever(mMoviesObserver);

        verify(mMoviesObserver).onChanged(MOVIES);

        mViewModel.sortMoviesByRating();

        verify(mErrorObserver).onChanged(any(Error.class));
    }

    @Test
    public void testSortMoviesByPopularity() {
        when(mMoviesRepository.getPopularMovies()).thenReturn(FLOWABLE_MOVIES);
        when(mMoviesRepository.getPopularMoviesSortedByPopularity()).thenReturn(FLOWABLE_MOVIES_SORTED);

        mViewModel.getPopularMovies().observeForever(mMoviesObserver);

        verify(mMoviesObserver).onChanged(MOVIES);

        mViewModel.sortMoviesByPopularity();

        verify(mMoviesObserver).onChanged(SORTED_MOVIES);
    }

    @Test
    public void testSortMoviesByPopularityWithError() {
        when(mMoviesRepository.getPopularMovies()).thenReturn(FLOWABLE_MOVIES);
        when(mMoviesRepository.getPopularMoviesSortedByPopularity()).thenReturn(FLOWABLE_ERROR);

        mViewModel.getPopularMovies().observeForever(mMoviesObserver);

        verify(mMoviesObserver).onChanged(MOVIES);

        mViewModel.sortMoviesByPopularity();

        verifyNoMoreInteractions(mMoviesObserver);

        verify(mErrorObserver).onChanged(any(Error.class));
    }

    @Test
    public void testReloadMovieWithError() {
        when(mMoviesRepository.reloadMovies()).thenReturn(FLOWABLE_ERROR);

        mViewModel.reloadMovies();

        verify(mErrorObserver).onChanged(any(Error.class));
    }

    private List<Movie> createMovieList() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie());
        movies.add(new Movie());

        return movies;
    }
}
