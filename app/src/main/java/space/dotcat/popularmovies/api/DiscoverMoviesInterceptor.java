package space.dotcat.popularmovies.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DiscoverMoviesInterceptor implements Interceptor {

    private static final String GREAT_BRITAIN_REGION = "GB";

    private static final String EXCLUDE_ADULTS_FLAG = "false";

    private static final String INCLUDE_VIDEOS_FLAG = "true";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl httpUrlWithQueries;

        httpUrlWithQueries = request.url();

        if (request.header("DiscoverMoviesRequest") != null) {
            httpUrlWithQueries = httpUrlWithQueries.newBuilder()
                    .addQueryParameter("region", GREAT_BRITAIN_REGION)
                    .addQueryParameter("include_adults", EXCLUDE_ADULTS_FLAG)
                    .addQueryParameter("include_videos", INCLUDE_VIDEOS_FLAG)
                    .build();
        }

        Request requestWithQueries = request.newBuilder()
                .url(httpUrlWithQueries)
                .build();

        return chain.proceed(requestWithQueries);
    }
}
