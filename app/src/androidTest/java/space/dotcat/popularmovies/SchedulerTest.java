package space.dotcat.popularmovies;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.State;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;
import androidx.work.test.TestDriver;
import androidx.work.test.WorkManagerTestInitHelper;
import io.reactivex.Flowable;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.MoviesDao;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.scheduler.WorkManagerScheduler;
import space.dotcat.popularmovies.utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SchedulerTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private Scheduler mScheduler;

    private MoviesDao mMoviesDao;

    private TestDriver mTestDriver;

    private static final List<Movie> MOVIES_WITHOUT_FLAGS = TestUtils.createMoviesWithoutFlags();

    private static final List<Movie> MOVIES = TestUtils.createMovieList();

    @Before
    public void init() {
        WorkManagerTestInitHelper.initializeTestWorkManager(AppDelegate.getInstance());

        mScheduler = new WorkManagerScheduler(WorkManager.getInstance());

        mMoviesDao = AppDelegate.getInstance().getAppLayerComponent().getFakeMoviesDao();

        mTestDriver = WorkManagerTestInitHelper.getTestDriver();
    }

    @After
    public void clear() {
        mMoviesDao.deleteAllMovies();
    }

    @Test
    public void testDeleteUnflaggedMoviesPeriodically() {
        mMoviesDao.insertMovies(MOVIES_WITHOUT_FLAGS);

        int moviesCount = mMoviesDao.getMoviesCount();

        assertEquals(MOVIES_WITHOUT_FLAGS.size(), moviesCount);

        LiveData<WorkStatus> info = mScheduler.startDeletingUnflagedMovies();

        info.observeForever(workStatus -> mTestDriver.setAllConstraintsMet(info.getValue().getId()));

        int moviesCountAfterScheduler = mMoviesDao.getMoviesCount();

        assertEquals(0, moviesCountAfterScheduler);
    }

    @Test
    public void testUpdateUpcomingMoviesPeriodically() {
        mMoviesDao.insertMovies(MOVIES);

        int count = mMoviesDao.getMoviesCount();

        assertEquals(4, count);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_ONGOING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1);

        LiveData<WorkStatus> info = mScheduler.startUpdatingUpcomingMovies();

        info.observeForever(workStatus -> mTestDriver.setAllConstraintsMet(workStatus.getId()));

        mMoviesDao.getMoviesByFlag(Movie.FLAG_UPCOMING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(4)
                .assertValueAt(1, movie-> movie.getTitle().equals("The Shawshank Redemption"));
    }

    @Test
    public void testUpdateOngoingMoviesPeriodically() {
        mMoviesDao.insertMovies(MOVIES);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_ONGOING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1);

        LiveData<WorkStatus> info = mScheduler.startUpdatingOngoingMovies();

        info.observeForever(workStatus -> mTestDriver.setAllConstraintsMet(workStatus.getId()));

        mMoviesDao.getMoviesByFlag(Movie.FLAG_ONGOING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(4);
    }

    @Test
    public void testUpdatePopularMoviesPeriodically() {
        mMoviesDao.insertMovies(MOVIES);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2);

        LiveData<WorkStatus> info = mScheduler.startUpdatingPopularMovies(3, 2, TimeUnit.DAYS);

        info.observeForever(workStatus -> mTestDriver.setAllConstraintsMet(workStatus.getId()));

        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(4);
    }

    @Test
    public void testReplaceUpdatingPopularMoviesWork() throws InterruptedException {
        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0);

        LiveData<WorkStatus> workStatusLiveData = mScheduler.startUpdatingPopularMovies(3,
                2, TimeUnit.DAYS);

        workStatusLiveData.observeForever(info-> {
            if (info != null) {
                Log.i("TEST", info.getState().toString());
            }
        });

        LiveData<WorkStatus> replaceWorkStatus = mScheduler.replaceUpdatingPopularMoviesWork(1,
                2, TimeUnit.DAYS);

        replaceWorkStatus.observeForever(workStatus -> mTestDriver.setAllConstraintsMet(workStatus.getId()));

        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(4);

        mTestDriver.setAllConstraintsMet(replaceWorkStatus.getValue().getId());

        WorkManager.getInstance()
                .getStatusesForUniqueWork("UPDATING POPULAR MOVIES WORK")
                .observeForever(list-> assertSame(1, list.size()));
    }
}
