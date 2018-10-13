package space.dotcat.popularmovies.di.appLayer;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import space.dotcat.popularmovies.AppDelegate;
import space.dotcat.popularmovies.di.appLayer.workers.WorkersModule;
import space.dotcat.popularmovies.di.presentationLayer.ActivityProviderModule;
import space.dotcat.popularmovies.di.workersInjection.WorkerInjectionModule;

@Component(modules = {AndroidInjectionModule.class,
        WorkerInjectionModule.class,
        AppModule.class,
        DatabaseModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        WorkersModule.class,
        ActivityProviderModule.class})
@Singleton
public interface AppLayerComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder addApplication(Application application);

        AppLayerComponent build();
    }

    void inject(AppDelegate app);
}
