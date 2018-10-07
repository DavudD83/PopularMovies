package space.dotcat.popularmovies;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import space.dotcat.popularmovies.di.appLayer.AppLayerComponent;
import space.dotcat.popularmovies.di.appLayer.AppModule;
import space.dotcat.popularmovies.di.appLayer.DaggerAppLayerComponent;
import space.dotcat.popularmovies.di.appLayer.DatabaseModule;
import space.dotcat.popularmovies.di.appLayer.NetworkModule;
import space.dotcat.popularmovies.di.appLayer.RepositoryModule;

public class AppDelegate extends Application implements HasActivityInjector {

    private static AppDelegate sInstance;

    private AppLayerComponent mAppLayerComponent;

    @Inject
    DispatchingAndroidInjector<Activity> mActivityDispatchingAndroidInjector;

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
}
