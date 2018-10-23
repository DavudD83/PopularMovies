package space.dotcat.popularmovies.di.appLayer;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import space.dotcat.popularmovies.AppDelegate;
import space.dotcat.popularmovies.api.ApiService;
import space.dotcat.popularmovies.di.appLayer.workers.WorkersModule;
import space.dotcat.popularmovies.di.presentationLayer.ActivityProviderModule;
import space.dotcat.popularmovies.di.workersInjection.WorkerInjectionModule;
import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;
import space.dotcat.popularmovies.repository.moviesRepository.localMoviesSource.MoviesDao;
import space.dotcat.popularmovies.scheduler.Scheduler;

@Component(modules = {
        AndroidInjectionModule.class,
        WorkerInjectionModule.class,
        AppModule.class,
        DatabaseModule.class,
        SharedPreferencesModule.class,
        NetworkModule.class,
        WorkersModule.class,
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

    KeyValueRepository getFakeKeyValueRepository();

    SharedPreferences getFakeSharedPreferences();
}
