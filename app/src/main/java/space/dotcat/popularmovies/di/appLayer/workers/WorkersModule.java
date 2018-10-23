package space.dotcat.popularmovies.di.appLayer.workers;

import androidx.work.Worker;
import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import space.dotcat.popularmovies.di.workersInjection.WorkerKey;
import space.dotcat.popularmovies.scheduler.workers.DeleteUnflagedMoviesWorker;
import space.dotcat.popularmovies.scheduler.workers.UpdateMoviesWorker;

@Module(
        subcomponents = {
                DeleteUnflagedMoviesWorkerSubcomponent.class,
                UpdateMoviesSubcomponent.class
        }
)
public abstract class WorkersModule {

    @Binds
    @IntoMap
    @WorkerKey(DeleteUnflagedMoviesWorker.class)
    abstract AndroidInjector.Factory<? extends Worker> bindDeleteMoviesWorker(DeleteUnflagedMoviesWorkerSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @WorkerKey(UpdateMoviesWorker.class)
    abstract AndroidInjector.Factory<? extends Worker> bindUpdateMoviesWorker(UpdateMoviesSubcomponent.Builder builder);
}
