package space.dotcat.popularmovies.di.appLayer;

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
    @Named("Auth")
    Interceptor provideAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Provides
    @Singleton
    @Named("DiscoverMovies")
    Interceptor provideDiscoverMoviesInterceptor() {
        return new DiscoverMoviesInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient provideClient(@Named("Auth") Interceptor authInterceptor,
                               @Named("DiscoverMovies") Interceptor discoverMoviesInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(discoverMoviesInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory gsonConverter, CallAdapter.Factory rxJava2Adapter,
                             OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(gsonConverter)
                .addCallAdapterFactory(rxJava2Adapter)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
