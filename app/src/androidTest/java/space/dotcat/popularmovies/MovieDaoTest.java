package space.dotcat.popularmovies;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Flowable;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.localMoviesSource.MoviesDao;
import space.dotcat.popularmovies.utils.TestUtils;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MovieDaoTest {

    private static final int INSERTED_MOVIE_ID = 1000;

    private static final String INSERTED_MOVIE_TITLE = "TITLE";

    private static final List<Movie> MOVIES = TestUtils.createMovieList();

    private static final List<Movie> MOVIES_WITHOUT_FLAGS = TestUtils.createMoviesWithoutFlags();

    private MoviesDao mMoviesDao;

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        mMoviesDao = AppDelegate.getInstance().getAppLayerComponent().getFakeMoviesDao();

        mMoviesDao.insertMovies(MOVIES);
    }

    @After
    public void clear() {
        mMoviesDao.deleteAllMovies();

        mMoviesDao = null;
    }

    @Test
    public void testSetAllMoviesFlagToFalse() {
        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.POPULAR_MOVIE_ID);

        String query = String.format(MoviesDao.QUERY_SET_ALL_MOVIES_FLAG_TO_FALSE, Movie.FLAG_POPULAR);

        SupportSQLiteQuery supportSQLiteQuery = new SimpleSQLiteQuery(query);

        mMoviesDao.setAllMoviesFlagToFalse(supportSQLiteQuery);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0);
    }

    @Test
    public void testSetMovieFlagToTrue() {
        testSetAllMoviesFlagToFalse();

        String setFlagToTrue = String.format(MoviesDao.QUERY_SET_MOVIE_FLAG_TO_TRUE, Movie.FLAG_POPULAR,
                TestUtils.POPULAR_MOVIE_ID);

        SupportSQLiteQuery supportSQLiteQuery1 = new SimpleSQLiteQuery(setFlagToTrue);

        mMoviesDao.setMovieFlagToTrue(supportSQLiteQuery1);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1);
    }

    @Test
    public void testInsertMovie() {
        mMoviesDao.getMoviesByFlag(Movie.FLAG_UPCOMING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.UPCOMING_MOVIE_ID);

        Movie insertedMovie = new Movie();
        insertedMovie.setId(INSERTED_MOVIE_ID);
        insertedMovie.setTitle(INSERTED_MOVIE_TITLE);
        insertedMovie.setUpcoming(true);

        mMoviesDao.insertMovie(insertedMovie);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_UPCOMING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(1, movie -> movie.getId() == INSERTED_MOVIE_ID &&
                        movie.getTitle().equals(INSERTED_MOVIE_TITLE));
    }

    @Test
    public void testInsertMovieWhenMovieExist() {
        testInsertMovie();

        Movie insertedMovie = new Movie();
        insertedMovie.setId(INSERTED_MOVIE_ID);
        insertedMovie.setTitle("ANOTHER_TITLE");
        insertedMovie.setUpcoming(true);

        long result = mMoviesDao.insertMovie(insertedMovie);

        assertEquals(-1, result);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_UPCOMING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(1, movie -> movie.getTitle().equals(INSERTED_MOVIE_TITLE));
    }

    @Test
    public void testDeleteAllMoviesWithoutFlags() {
        mMoviesDao.insertMovies(MOVIES_WITHOUT_FLAGS);

        int deletedMovies = mMoviesDao.deleteMoviesWithoutFlags();

        assertEquals(MOVIES_WITHOUT_FLAGS.size(), deletedMovies);

        mMoviesDao.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1);
    }
}
