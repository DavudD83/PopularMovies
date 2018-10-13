package space.dotcat.popularmovies.di.workersInjection;

import androidx.work.Worker;
import dagger.android.AndroidInjector;

public interface HasWorkerInjector {

    AndroidInjector<Worker> hasWorkerInjector();
}
