package space.dotcat.popularmovies.utils;

import java.util.Arrays;
import java.util.List;

import space.dotcat.popularmovies.RemoteSourceTest;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.model.Video;

public class TestUtils {

    public static final String POPULAR_MOVIE_TITLE = "Avatar";

    public static final String UPDATED_TITLE = "Avatar-cool";

    public static final int POPULAR_MOVIE_ID = 100;

    public static final int POPULAR_MOVIE1_ID = 200;

    public static final int UPCOMING_MOVIE_ID = 278;

    public static final int ONGOING_MOVIE_ID = 300;

    public static final float POPULAR_MOVIE_VOTE_AVERAGE = 3.2f;

    public static final float POPULAR_MOVIE1_VOTE_AVERAGE = 4.6f;

    public static final float POPULAR_MOVIE_POPULARITY = 33.2f;

    public static final float POPULAR_MOVIE1_POPULARITY = 22.2f;

    public static List<Movie> createMovieList() {
        Movie popularMovie = new Movie();
        popularMovie.setId(POPULAR_MOVIE_ID);
        popularMovie.setPopular(true);
        popularMovie.setTitle(POPULAR_MOVIE_TITLE);
        popularMovie.setVote_average(POPULAR_MOVIE_VOTE_AVERAGE);
        popularMovie.setPopularity(POPULAR_MOVIE_POPULARITY);
        popularMovie.setReleaseDate("2018-10-14");

        Movie popularMovie1 = new Movie();
        popularMovie1.setId(POPULAR_MOVIE1_ID);
        popularMovie1.setPopular(true);
        popularMovie1.setVote_average(POPULAR_MOVIE1_VOTE_AVERAGE);
        popularMovie1.setPopularity(POPULAR_MOVIE1_POPULARITY);
        popularMovie1.setReleaseDate("2018-10-03");


        Movie upcomingMovie = new Movie();
        upcomingMovie.setId(UPCOMING_MOVIE_ID);
        upcomingMovie.setUpcoming(true);

        Movie ongoingMovie = new Movie();
        ongoingMovie.setId(ONGOING_MOVIE_ID);
        ongoingMovie.setOngoing(true);
        ongoingMovie.setIsFavorite(true);

        return Arrays.asList(popularMovie, popularMovie1, upcomingMovie, ongoingMovie);
    }

    public static List<Video> createVideoList() {
        Video video1 = new Video();
        video1.setId("100");
        video1.setMovieId(UPCOMING_MOVIE_ID);
        video1.setKey("key");

        Video video2 = new Video();
        video2.setId("200");
        video2.setMovieId(UPCOMING_MOVIE_ID);

        Video video3 = new Video();
        video3.setId("300");
        video3.setMovieId(POPULAR_MOVIE_ID);

        return Arrays.asList(video1, video2, video3);
    }

    public static List<Review> createReviewList() {
        Review review1 = new Review();
        review1.setId("100");
        review1.setMovieId(UPCOMING_MOVIE_ID);

        Review review2 = new Review();
        review2.setId("200");
        review2.setMovieId(UPCOMING_MOVIE_ID);

        Review review3 = new Review();
        review3.setId("300");
        review3.setMovieId(POPULAR_MOVIE_ID);

        return Arrays.asList(review1, review2, review3);
    }
}
