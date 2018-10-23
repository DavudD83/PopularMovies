package space.dotcat.popularmovies.di.workersInjection;

import java.util.Map;

import androidx.work.Worker;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.Multibinds;

@Module
public abstract class WorkerInjectionModule {

    @Multibinds
    abstract Map<Class<? extends Worker>, AndroidInjector.Factory<? extends Worker>> workerInjectorFactories();
}
