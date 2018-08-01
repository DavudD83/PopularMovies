package space.dotcat.popularmovies.viewModels;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import space.dotcat.popularmovies.model.Error;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.base.BaseViewModel;
import space.dotcat.popularmovies.utils.RxJavaTestRule;

@RunWith(JUnit4.class)
public abstract class BaseViewModelTest<VM extends BaseViewModel> {

    @Mock
    public MoviesRepository mMoviesRepository;

    @Mock
    public Observer<List<Movie>> mMoviesObserver;

    @Mock
    public Observer<Error> mErrorObserver;

    @Mock
    public Observer<Boolean> mLoadingObserver;

    @Rule
    public InstantTaskExecutorRule mTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public RxJavaTestRule mRxJavaTestRule = new RxJavaTestRule();

    protected VM mViewModel;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mViewModel = createViewModelForTesting();

        mViewModel.observeLoadingIndicator().observeForever(mLoadingObserver);
        mViewModel.observeErrors().observeForever(mErrorObserver);
    }

    @After
    public void clear() {
        mViewModel.observeLoadingIndicator().removeObserver(mLoadingObserver);
        mViewModel.observeErrors().removeObserver(mErrorObserver);
    }

    protected abstract VM createViewModelForTesting();
}
