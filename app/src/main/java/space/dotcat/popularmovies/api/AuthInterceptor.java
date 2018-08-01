package space.dotcat.popularmovies.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import space.dotcat.popularmovies.BuildConfig;

public class AuthInterceptor implements Interceptor {

    public AuthInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl urlWithKey = request.url().newBuilder()
                .addQueryParameter("api_key", BuildConfig.AUTH_KEY)
                .build();

        Request requestWithKey = request.newBuilder()
                .url(urlWithKey)
                .build();

        return chain.proceed(requestWithKey);
    }
}
