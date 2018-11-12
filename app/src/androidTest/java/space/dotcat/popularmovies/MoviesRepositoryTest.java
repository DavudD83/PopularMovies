package space.dotcat.popularmovies;

import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import space.dotcat.popularmovies.api.RequestMatcher;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.MoviesDao;
import space.dotcat.popularmovies.repository.moviesRepository.remoteMoviesSource.RemoteMoviesSourceImpl;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.utils.TestUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MoviesRepositoryTest {

    private MoviesRepository mMoviesRepository;

    private MoviesDao mMoviesDao;

    private SharedPreferences mSharedPreferences;

    private static final List<Review> REVIEWS = TestUtils.createReviewList();

    private static final String INTERNET_CONNECTION_ERROR = "HTTP 500 Internet connection error";

    @Before
    public void init() {
        mMoviesRepository = AppDelegate.getInstance().getAppLayerComponent().getFakeMoviesRepo();
        mMoviesDao = AppDelegate.getInstance().getAppLayerComponent().getFakeMoviesDao();
        mSharedPreferences = AppDelegate.getInstance().getAppLayerComponent().getFakeSharedPreferences();

        mMoviesDao.insertMovies(TestUtils.createMovieList());
    }

    @After
    public void after() {
        mMoviesDao.deleteAllMovies();

        mSharedPreferences.edit().clear().commit();
    }

    @Test
    public void testGetTrailersAndReviews() {
        mMoviesRepository.getTrailersAndReviews(TestUtils.UPCOMING_MOVIE_ID)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline(), true)
                .test()
                .assertValueCount(1)
                .assertValue(movieExtraInfoResult ->
                movieExtraInfoResult.getReviewList().size() == 4 &&
                        movieExtraInfoResult.getTrailer().getType()
                                .equals(RemoteMoviesSourceImpl.TRAILER_VIDEO_TYPE));

        mMoviesRepository.getReviews(TestUtils.UPCOMING_MOVIE_ID)
                .toFlowable()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertValueCount(4);

        mMoviesRepository.getTrailer(TestUtils.UPCOMING_MOVIE_ID)
                .test()
                .assertValueCount(1);
    }

    @Test
    public void testGetTrailersAndReviewsWithInternetErrorEmptyTrailerLocally() {
        mSharedPreferences.edit().putBoolean(RequestMatcher.IS_ERROR, true).commit();

        mMoviesDao.insertReviews(REVIEWS);

        mMoviesRepository.getTrailersAndReviews(TestUtils.UPCOMING_MOVIE_ID)
                .subscribeOn(Schedulers.trampoline())
                .test()
                .assertValueCount(1)
                .assertValue(info-> info.getReviewList().size() == 2 && info.getTrailer() == null)
                .assertError(error-> {
                    String message = error.getMessage();

                    return message.equals(INTERNET_CONNECTION_ERROR);
                });
    }

    @Test
    public void testGetTrailersAndReviewsWithInternetErrorEmptyReviewsLocally() {
        mSharedPreferences.edit().putBoolean(RequestMatcher.IS_ERROR, true).commit();

        mMoviesDao.insertTrailer(TestUtils.createVideoList().get(0));

        mMoviesRepository.getTrailersAndReviews(TestUtils.UPCOMING_MOVIE_ID)
                .test()
                .assertValueCount(1)
                .assertValue(info-> info.getReviewList().size() == 0 && info.getTrailer().getKey().equals("key"))
                .assertError(error-> error.getMessage().equals(INTERNET_CONNECTION_ERROR));
    }

    @Test
    public void testGetTrailersAndReviewsWithInternetErrorEmptyData() {
        mSharedPreferences.edit().putBoolean(RequestMatcher.IS_ERROR, true).commit();

        mMoviesRepository.getTrailersAndReviews(TestUtils.UPCOMING_MOVIE_ID)
                .test()
                .assertValueCount(1)
                .assertValue(info-> info.getReviewList().size() == 0 && info.getTrailer() == null)
                .assertError(error-> error.getMessage().equals(INTERNET_CONNECTION_ERROR));
    }

    @Test
    public void testGetTrailersAndReviewsWithInternetErrorHasData() {
        mSharedPreferences.edit().putBoolean(RequestMatcher.IS_ERROR, true).commit();

        mMoviesDao.insertTrailer(TestUtils.createVideoList().get(0));
        mMoviesDao.insertReviews(REVIEWS);

        mMoviesRepository.getTrailersAndReviews(TestUtils.UPCOMING_MOVIE_ID)
                .test()
                .assertValueCount(1)
                .assertValue(info-> info.getReviewList().size() == 2 && info.getTrailer().getKey().equals("key"))
                .assertError(error-> error.getMessage().equals(INTERNET_CONNECTION_ERROR));
    }
}
