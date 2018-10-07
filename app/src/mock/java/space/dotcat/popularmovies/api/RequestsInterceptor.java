package space.dotcat.popularmovies.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestsInterceptor implements Interceptor {

    private RequestMatcher mRequestMatcher;

    public RequestsInterceptor(RequestMatcher requestMatcher) {
        mRequestMatcher = requestMatcher;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl httpUrl = request.url();

        String path;

        if (httpUrl.encodedPathSegments().size() > 3) {
            path = httpUrl.encodedPathSegments().get(3);
        } else {
            path = httpUrl.pathSegments().get(2);
        }

        if (mRequestMatcher.isMatch(path)) {
            return mRequestMatcher.getResponseForRequest(path, request);
        }

        return chain.proceed(request);
    }
}
