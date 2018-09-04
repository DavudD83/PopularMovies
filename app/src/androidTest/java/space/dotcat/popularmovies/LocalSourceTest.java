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
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.repository.localMoviesSource.LocalMoviesSourceImpl;
import space.dotcat.popularmovies.repository.remoteMoviesSource.RemoteMoviesSourceImpl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class LocalSourceTest {

    private static final List<Movie> MOVIES = createMovieList();

    private static final List<Review> REVIEWS = createReviewList();

    private static final List<Video> VIDEOS = createVideoList();

    private static List<Video> createVideoList() {
        Video video1 = new Video();
        video1.setId("100");
        video1.setMovieId(RemoteSourceTest.MOVIE_ID);
        video1.setKey("key");

        Video video2 = new Video();
        video2.setId("200");
        video2.setMovieId(RemoteSourceTest.MOVIE_ID);

        Video video3 = new Video();
        video3.setId("300");
        video3.setMovieId(100);

        return Arrays.asList(video1, video2, video3);
    }

    private static List<Review> createReviewList() {
        Review review1 = new Review();
        review1.setId("100");
        review1.setMovieId(RemoteSourceTest.MOVIE_ID);

        Review review2 = new Review();
        review2.setId("200");
        review2.setMovieId(RemoteSourceTest.MOVIE_ID);

        Review review3 = new Review();
        review3.setId("300");
        review3.setMovieId(100);

        return Arrays.asList(review1, review2, review3);
    }

    private static List<Movie> createMovieList() {
        Movie movie1 = new Movie();
        movie1.setId(100);

        Movie movie2 = new Movie();
        movie2.setId(RemoteSourceTest.MOVIE_ID);

        Movie movie3 = new Movie();
        movie3.setId(300);
        movie3.setIsFavorite(true);

        return Arrays.asList(movie1, movie2, movie3);
    }

    private MoviesRepository mLocalSource;

    @Rule
    public InstantTaskExecutorRule mTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        mLocalSource = new LocalMoviesSourceImpl(AppDelegate.getInstance().getAppLayerComponent().getFakeMoviesDao());

        mLocalSource.addMoviesSync(MOVIES);
    }

    @After
    public void clear() {
        mLocalSource = null;
    }

    @Test
    public void testLocalSourceWasCreated() {
        assertNotNull(mLocalSource);
    }

    @Test
    public void testGetPopularMovies() {
        mLocalSource.getPopularMovies()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(3)
                .assertValueAt(0, movie-> movie.getId() == 100);
    }

    @Test
    public void testGetFavoriteMovies() {
        mLocalSource.getFavoriteMovies()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1)
                .assertValue(movie-> movie.getId() == 300);
    }

    @Test
    public void testDeleteAllMovies() {
        mLocalSource.getPopularMovies()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(3);

        mLocalSource.deleteAllMoviesSync();

        mLocalSource.getPopularMovies()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(0);
    }

    @Test
    public void testGetMovieById() {
        Observer<Movie> movieObserver = movie -> assertEquals(RemoteSourceTest.MOVIE_ID, movie.getId());

        LiveData<Movie> movieLiveData = mLocalSource.getMovieById(RemoteSourceTest.MOVIE_ID);

        movieLiveData.observeForever(movieObserver);

        movieLiveData.removeObserver(movieObserver);
    }

    @Test
    public void testGetTrailerAndReviews() {
        mLocalSource.addReviewsSync(REVIEWS);

        mLocalSource.addTrailerSync(VIDEOS.get(0), VIDEOS.get(1), VIDEOS.get(2));

        mLocalSource.getTrailersAndReviews(RemoteSourceTest.MOVIE_ID)
                .test()
                .assertValue(info-> info.getTrailer().getKey().equals("key")
                        && info.getReviewList().size() == 2);
    }

    @Test
    public void testUpdateMovie() {
        mLocalSource.getFavoriteMovies()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(1);

        Movie updatedMovie = MOVIES.get(0);
        updatedMovie.setIsFavorite(true);

        mLocalSource.updateMovie(updatedMovie)
                .test()
                .assertNoErrors()
                .assertComplete();

        mLocalSource.getFavoriteMovies()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(2);
    }
}
