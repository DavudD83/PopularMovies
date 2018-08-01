package space.dotcat.popularmovies.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import space.dotcat.popularmovies.model.MovieResponse;
import space.dotcat.popularmovies.model.ReviewResponse;
import space.dotcat.popularmovies.model.VideoResponse;

public interface ApiService {

    @GET("popular")
    Single<MovieResponse> getPopularMovies();

    @GET("{movie_id}/videos")
    Single<VideoResponse> getVideos(@Path("movie_id") int movie_id);

    @GET("{movie_id}/reviews")
    Single<ReviewResponse> getReviews(@Path("movie_id") int movie_id);
}
