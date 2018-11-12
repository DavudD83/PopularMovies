package space.dotcat.popularmovies;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import androidx.work.Worker;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import space.dotcat.popularmovies.di.appLayer.AppLayerComponent;
import space.dotcat.popularmovies.di.appLayer.DaggerAppLayerComponent;
import space.dotcat.popularmovies.di.workersInjection.HasWorkerInjector;

public class AppDelegate extends Application implements HasActivityInjector, HasWorkerInjector {

    private static AppDelegate sInstance;

    private AppLayerComponent mAppLayerComponent;

    @Inject
    DispatchingAndroidInjector<Activity> mActivityDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Worker> mWorkerDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        buildAppComponent();

        mAppLayerComponent.inject(this);
    }

    private void buildAppComponent() {
        mAppLayerComponent = DaggerAppLayerComponent.builder()
                .addApplication(this)
                .build();
    }

    public AppLayerComponent getAppLayerComponent() {
        return mAppLayerComponent;
    }

    public static AppDelegate getInstance() {
        return sInstance;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mActivityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Worker> hasWorkerInjector() {
        return mWorkerDispatchingAndroidInjector;
    }
}
