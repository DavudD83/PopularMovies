package space.dotcat.popularmovies;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Flowable;
import io.reactivex.Single;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.repository.moviesRepository.remoteMoviesSource.RemoteMoviesSource;
import space.dotcat.popularmovies.repository.moviesRepository.remoteMoviesSource.RemoteMoviesSourceImpl;
import space.dotcat.popularmovies.utils.TestUtils;
import space.dotcat.popularmovies.utils.date.CalendarDateProvider;
import space.dotcat.popularmovies.utils.date.DateProvider;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RemoteSourceTest {

    private RemoteMoviesSource mRemoteSource;

    @Before
    public void init() {
        DateProvider dateProvider = new CalendarDateProvider();

        mRemoteSource = new RemoteMoviesSourceImpl(AppDelegate.getInstance().getAppLayerComponent().getFakeApiService(),
                dateProvider);
    }

    @After
    public void clear() {
        mRemoteSource = null;
    }

    @Test
    public void testRemoteSourceCreated() {
        assertNotNull(mRemoteSource);
    }

    @Test
    public void testLoadPopularMoviesFromTheInternet() {
        mRemoteSource.reloadMoviesWithFlag(Movie.FLAG_POPULAR)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertNoErrors()
                .assertValueAt(0, movie -> movie.getId() == 278 && movie.isPopular())
                .assertValueAt(1, movie -> movie.getId() == 250 && movie.isPopular());
    }

    @Test
    public void testLoadUpcomingMoviesFromTheInternet() {
        mRemoteSource.reloadMoviesWithFlag(Movie.FLAG_UPCOMING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertNoErrors()
                .assertValueAt(0, movie -> movie.getId() == 278 && movie.isUpcoming())
                .assertValueAt(1, movie -> movie.getId() == 250 && movie.isUpcoming());
    }

    @Test
    public void testLoadOngoingMoviesFromTheInternet() {
        mRemoteSource.reloadMoviesWithFlag(Movie.FLAG_ONGOING)
                .flatMap(Flowable::fromIterable)
                .test()
                .assertNoErrors()
                .assertValueAt(0, movie -> movie.getId() == 278 && movie.isOngoing())
                .assertValueAt(1, movie -> movie.getId() == 250 && movie.isOngoing());
    }

    @Test
    public void testLoadTrailer() {
        mRemoteSource.getTrailer(TestUtils.UPCOMING_MOVIE_ID)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(video -> video.get(0).getType().equals(RemoteMoviesSourceImpl.TRAILER_VIDEO_TYPE));
    }

    @Test
    public void testLoadReviews() {
        mRemoteSource.getReviews(TestUtils.UPCOMING_MOVIE_ID)
                .flattenAsFlowable(reviews -> reviews)
                .test()
                .assertNoErrors()
                .assertValueCount(4)
                .assertValueAt(0, review -> review.getMovieId() == TestUtils.UPCOMING_MOVIE_ID)
                .assertValueAt(1, review -> review.getMovieId() == TestUtils.UPCOMING_MOVIE_ID);
    }

    @Test
    public void testLoadTrailersAndReviews() {
        mRemoteSource.getTrailersAndReviews(TestUtils.UPCOMING_MOVIE_ID)
                .test()
                .assertValue(movieExtraInfo -> movieExtraInfo.getTrailer().getType()
                        .equals(RemoteMoviesSourceImpl.TRAILER_VIDEO_TYPE))
                .assertValue(movieExtraInfo -> movieExtraInfo.getReviewList().size() == 4 &&
                        movieExtraInfo.getReviewList().get(0).getMovieId() == TestUtils.UPCOMING_MOVIE_ID);
    }
}
