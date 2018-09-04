package space.dotcat.popularmovies.di.appLayer;

import android.content.Context;

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
import space.dotcat.popularmovies.api.RequestMatcher;
import space.dotcat.popularmovies.api.RequestsInterceptor;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Auth;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Request;

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
    RequestMatcher provideRequestMatcher(Context context) {
        return new RequestMatcher(context);
    }

    @Provides
    @Singleton
    @Auth
    Interceptor provideAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Provides
    @Singleton
    @Request
    Interceptor provideRequestInterceptor(RequestMatcher requestMatcher) {
        return new RequestsInterceptor(requestMatcher);
    }

    @Provides
    @Singleton
    OkHttpClient provideClient(@Auth Interceptor authInterceptor,@Request Interceptor requestInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
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
