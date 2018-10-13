package space.dotcat.popularmovies.di.workersInjection;

import android.content.Context;

import androidx.work.Worker;
import dagger.android.AndroidInjector;
import space.dotcat.popularmovies.AppDelegate;

public class WorkerInjection {

    public static <W extends Worker> void inject(W worker) {
        if (worker == null) {
            throw new IllegalArgumentException("Worker that has been passed to inject can not be null");
        }

        Context appContext = worker.getApplicationContext();

        if (!(appContext instanceof HasWorkerInjector)) {
            throw new IllegalArgumentException("Application class does not implement HasWorkerInjector");
        }

        AndroidInjector<Worker> workerAndroidInjector = ((HasWorkerInjector) appContext).hasWorkerInjector();

        if (workerAndroidInjector == null) {
            throw new IllegalStateException("Application class has returned null worker injector");
        }

        workerAndroidInjector.inject(worker);
    }
}
