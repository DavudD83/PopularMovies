package space.dotcat.popularmovies.di.appLayer.workers;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import space.dotcat.popularmovies.scheduler.workers.UpdateMoviesWorker;

@Subcomponent
public interface UpdateMoviesSubcomponent extends AndroidInjector<UpdateMoviesWorker> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<UpdateMoviesWorker> {
    }
}
