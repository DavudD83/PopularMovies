package space.dotcat.popularmovies;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.repository.localMoviesSource.LocalMoviesSource;
import space.dotcat.popularmovies.repository.localMoviesSource.LocalMoviesSourceImpl;
import space.dotcat.popularmovies.repository.remoteMoviesSource.RemoteMoviesSourceImpl;
import space.dotcat.popularmovies.utils.TestUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class LocalSourceTest {

    private static final List<Movie> MOVIES = TestUtils.createMovieList();

    private static final List<Review> REVIEWS = TestUtils.createReviewList();

    private static final List<Video> VIDEOS = TestUtils.createVideoList();

    private LocalMoviesSource mLocalSource;

    @Rule
    public InstantTaskExecutorRule mTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        mLocalSource = new LocalMoviesSourceImpl(AppDelegate.getInstance().getAppLayerComponent().getFakeMoviesDao());

        mLocalSource.addMoviesSync(MOVIES);
    }

    @After
    public void clear() {
        mLocalSource.deleteAllMovies();

        mLocalSource = null;
    }

    @Test
    public void testLocalSourceWasCreated() {
        assertNotNull(mLocalSource);
    }

    @Test
    public void testGetPopularMovies() {
        mLocalSource.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.POPULAR_MOVIE_ID &&
                        movie.getTitle().equals(TestUtils.POPULAR_MOVIE_TITLE))
                .assertValueAt(1, movie -> movie.getId() == TestUtils.POPULAR_MOVIE1_ID);
    }

    @Test
    public void testGetUpcomingMovies() {
        mLocalSource.getMoviesByFlag(Movie.FLAG_UPCOMING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1)
                .assertValue(movie -> movie.getId() == TestUtils.UPCOMING_MOVIE_ID);
    }

    @Test
    public void testGetOngoingMovies() {
        mLocalSource.getMoviesByFlag(Movie.FLAG_ONGOING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1)
                .assertValue(movie -> movie.getId() == TestUtils.ONGOING_MOVIE_ID);
    }

    @Test
    public void testGetFavoriteMovies() {
        mLocalSource.getMoviesByFlag(Movie.FLAG_FAVORITE)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.ONGOING_MOVIE_ID && movie.isFavorite());
    }

    @Test
    public void testGetMovieById() {
        Observer<Movie> movieObserver = movie -> assertEquals(TestUtils.UPCOMING_MOVIE_ID, movie.getId());

        LiveData<Movie> movieLiveData = mLocalSource.getMovieById(TestUtils.UPCOMING_MOVIE_ID);

        movieLiveData.observeForever(movieObserver);

        movieLiveData.removeObserver(movieObserver);
    }

    @Test
    public void testGetTrailerAndReviews() {
        mLocalSource.addReviewsSync(REVIEWS);

        mLocalSource.addTrailerSync(VIDEOS.get(0), VIDEOS.get(1), VIDEOS.get(2));

        mLocalSource.getTrailersAndReviews(TestUtils.UPCOMING_MOVIE_ID)
                .test()
                .assertValue(info-> info.getTrailer().getKey().equals("key")
                        && info.getReviewList().size() == 2);
    }

    @Test
    public void testUpdateMovie() {
        mLocalSource.getMoviesByFlag(Movie.FLAG_FAVORITE)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1);

        Movie updatedMovie = new Movie();
        updatedMovie.setId(TestUtils.POPULAR_MOVIE_ID);
        updatedMovie.setIsFavorite(true);
        updatedMovie.setPopular(true);

        mLocalSource.updateMovie(updatedMovie)
                .test()
                .assertNoErrors()
                .assertComplete();

        mLocalSource.getMoviesByFlag(Movie.FLAG_FAVORITE)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2);
    }

    @Test
    public void updateReloadedMoviesWhenMovieAlreadyInDb() {
        mLocalSource.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.POPULAR_MOVIE_ID &&
                        movie.getTitle().equals(TestUtils.POPULAR_MOVIE_TITLE))
                .assertValueAt(1, movie -> movie.getId() == TestUtils.POPULAR_MOVIE1_ID);

        Movie updatedPopularMovie = new Movie();
        updatedPopularMovie.setId(TestUtils.POPULAR_MOVIE_ID);
        updatedPopularMovie.setTitle(TestUtils.UPDATED_TITLE);
        updatedPopularMovie.setPopular(true);

        mLocalSource.updateReloadedMoviesSync(Collections.singletonList(updatedPopularMovie), Movie.FLAG_POPULAR);

        mLocalSource.getMoviesByFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1)
                .assertValueAt(0, movie-> movie.getId() == TestUtils.POPULAR_MOVIE_ID &&
                        movie.getTitle().equals(TestUtils.UPDATED_TITLE));
    }

    @Test
    public void testGetMoviesWithFlagSortedByRating() {
        mLocalSource.getMoviesWithFlagSortedByRating(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.POPULAR_MOVIE1_ID)
                .assertValueAt(1, movie -> movie.getId() == TestUtils.POPULAR_MOVIE_ID);
    }

    @Test
    public void testGetMoviesWithFlagSortedByPopularity() {
        mLocalSource.getMoviesWithFlagSortedByPopularity(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.POPULAR_MOVIE_ID)
                .assertValueAt(1, movie -> movie.getId() == TestUtils.POPULAR_MOVIE1_ID);
    }

    @Test
    public void testGetMoviesWithFlagSortedByDate() {
        mLocalSource.getMoviesWithFlagSortedByReleaseDate(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2)
                .assertValueAt(0, movie -> movie.getId() == TestUtils.POPULAR_MOVIE1_ID)
                .assertValueAt(1, movie -> movie.getId() == TestUtils.POPULAR_MOVIE_ID);
    }
}
