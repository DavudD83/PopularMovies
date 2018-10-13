package space.dotcat.popularmovies.di.workersInjection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.work.Worker;
import dagger.MapKey;

@MapKey
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkerKey {

    Class<? extends Worker> value();
}
