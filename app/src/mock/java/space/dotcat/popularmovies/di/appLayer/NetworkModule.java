package space.dotcat.popularmovies.di.appLayer;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import space.dotcat.popularmovies.BuildConfig;
import space.dotcat.popularmovies.api.ApiService;
import space.dotcat.popularmovies.api.AuthInterceptor;
import space.dotcat.popularmovies.api.DiscoverMoviesInterceptor;
import space.dotcat.popularmovies.api.RequestMatcher;
import space.dotcat.popularmovies.api.RequestsInterceptor;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Converter.Factory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    CallAdapter.Factory provideRxJava2AdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    RequestMatcher provideRequestMatcher(Context context, SharedPreferences sharedPreferences) {
        return new RequestMatcher(context, sharedPreferences);
    }

    @Provides
    @Singleton
    @Named("Auth")
    Interceptor provideAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Provides
    @Singleton
    @Named("Discover")
    Interceptor provideDiscoverInterceptor() {
        return new DiscoverMoviesInterceptor();
    }

    @Provides
    @Singleton
    @Named("Request")
    Interceptor provideRequestInterceptor(RequestMatcher requestMatcher) {
        return new RequestsInterceptor(requestMatcher);
    }

    @Provides
    @Singleton
    OkHttpClient provideClient(@Named("Auth") Interceptor authInterceptor, @Named("Discover") Interceptor discoveryInterceptor,
                               @Named("Request") Interceptor requestInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(discoveryInterceptor)
                .addInterceptor(requestInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory gsonConverter, CallAdapter.Factory rxJava2Adapter,
                             OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(gsonConverter)
                .addCallAdapterFactory(rxJava2Adapter)
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
