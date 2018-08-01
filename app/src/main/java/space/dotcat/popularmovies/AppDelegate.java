package space.dotcat.popularmovies;

import android.app.Application;

import space.dotcat.popularmovies.di.appLayer.AppLayerComponent;
import space.dotcat.popularmovies.di.appLayer.AppModule;
import space.dotcat.popularmovies.di.appLayer.DaggerAppLayerComponent;
import space.dotcat.popularmovies.di.appLayer.DatabaseModule;
import space.dotcat.popularmovies.di.appLayer.NetworkModule;
import space.dotcat.popularmovies.di.appLayer.RepositoryModule;

public class AppDelegate extends Application {

    private static AppDelegate sInstance;

    private AppLayerComponent mAppLayerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        buildAppComponent();
    }

    private void buildAppComponent() {
        mAppLayerComponent = DaggerAppLayerComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .databaseModule(new DatabaseModule())
                .networkModule(new NetworkModule())
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public AppLayerComponent getAppLayerComponent() {
        return mAppLayerComponent;
    }

    public static AppDelegate getInstance() {
        return sInstance;
    }
}
