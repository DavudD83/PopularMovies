package space.dotcat.popularmovies;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Flowable;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.repository.remoteMoviesSource.RemoteMoviesSourceImpl;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RemoteSourceTest {

    public static final int MOVIE_ID = 278;

    private MoviesRepository mRemoteSource;

    @Before
    public void init() {
        mRemoteSource = new RemoteMoviesSourceImpl(AppDelegate.getInstance().getAppLayerComponent().getFakeApiService());
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
        mRemoteSource.getPopularMovies()
                .flatMap(Flowable::fromIterable)
                .test()
                .assertNoErrors()
                .assertValueCount(4)
                .assertValueAt(0, movie-> movie.getId() == 278)
                .assertValueAt(1, movie-> movie.getId() == 250);
    }

    @Test
    public void testLoadTrailer() {
        mRemoteSource.getTrailer(MOVIE_ID)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(video -> video.getType().equals(RemoteMoviesSourceImpl.TRAILER_VIDEO_TYPE));
    }

    @Test
    public void testLoadReviews() {
        mRemoteSource.getReviews(MOVIE_ID)
                .flattenAsFlowable(reviews -> reviews)
                .test()
                .assertNoErrors()
                .assertValueCount(4)
                .assertValueAt(0, review -> review.getMovieId() == MOVIE_ID)
                .assertValueAt(1, review -> review.getMovieId() == MOVIE_ID);
    }

    @Test
    public void testLoadTrailersAndReviews() {
        mRemoteSource.getTrailersAndReviews(MOVIE_ID)
                .test()
                .assertValue(movieExtraInfo -> movieExtraInfo.getTrailer().getType()
                        .equals(RemoteMoviesSourceImpl.TRAILER_VIDEO_TYPE))
                .assertValue(movieExtraInfo -> movieExtraInfo.getReviewList().size() == 4 &&
                        movieExtraInfo.getReviewList().get(0).getMovieId() == MOVIE_ID);
    }
}
