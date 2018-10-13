package space.dotcat.popularmovies.di.appLayer;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import androidx.work.WorkManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.scheduler.WorkManagerScheduler;
import space.dotcat.popularmovies.utils.date.CalendarDateProvider;
import space.dotcat.popularmovies.utils.date.DateProvider;
import space.dotcat.popularmovies.utils.image.ImageLoader;
import space.dotcat.popularmovies.utils.image.PicassoImageLoader;

@Module
public abstract class AppModule {

    @Binds
    @Singleton
    abstract Context provideContext(Application application);

    @Binds
    @Singleton
    abstract ImageLoader provideImageLoader(PicassoImageLoader picassoImageLoader);

    @Binds
    @Singleton
    abstract DateProvider provideDateProvider(CalendarDateProvider calendarDateProvider);

    @Provides
    static WorkManager provideWorkManager() {
        return WorkManager.getInstance();
    }

    @Binds
    abstract Scheduler provieWorkManagerScheduler(WorkManagerScheduler workManagerScheduler);
}
