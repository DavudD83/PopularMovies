package space.dotcat.popularmovies.di.appLayer.workers;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import space.dotcat.popularmovies.scheduler.workers.DeleteUnflagedMoviesWorker;

@Subcomponent
public interface DeleteUnflagedMoviesWorkerSubcomponent extends AndroidInjector<DeleteUnflagedMoviesWorker> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DeleteUnflagedMoviesWorker> {
    }
}
