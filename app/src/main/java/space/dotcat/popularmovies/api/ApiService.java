package space.dotcat.popularmovies.api;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import space.dotcat.popularmovies.model.MovieResponse;
import space.dotcat.popularmovies.model.ReviewResponse;
import space.dotcat.popularmovies.model.VideoResponse;

public interface ApiService {

    @GET("discover/movie?sort_by=popularity.desc")
    @Headers("DiscoverMoviesRequest: true")
    Single<MovieResponse> getPopularMovies();

    @GET("discover/movie")
    @Headers("DiscoverMoviesRequest: true")
    Single<MovieResponse> getOngoingMovies(@QueryMap HashMap<String, String> complexQuery);

    @GET("discover/movie")
    @Headers("DiscoverMoviesRequest: true")
    Single<MovieResponse> getUpcomingMovies(@QueryMap HashMap<String, String> complexQuery);

    @GET("movie/{movie_id}/videos")
    Single<VideoResponse> getVideos(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/reviews")
    Single<ReviewResponse> getReviews(@Path("movie_id") int movie_id);
}
