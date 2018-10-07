package space.dotcat.popularmovies.di.appLayer;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import space.dotcat.popularmovies.AppDelegate;
import space.dotcat.popularmovies.api.ApiService;
import space.dotcat.popularmovies.di.presentationLayer.ActivityProviderModule;
import space.dotcat.popularmovies.repository.localMoviesSource.MoviesDao;

@Component(modules = {AndroidInjectionModule.class,
        AppModule.class,
        DatabaseModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        ActivityProviderModule.class})
@Singleton
public interface AppLayerComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder addApplication(Application application);

        AppLayerComponent build();
    }

    void inject(AppDelegate appDelegate);

    ApiService getFakeApiService();

    MoviesDao getFakeMoviesDao();
}
