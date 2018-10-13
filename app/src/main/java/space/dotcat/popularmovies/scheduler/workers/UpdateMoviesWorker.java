package space.dotcat.popularmovies.scheduler.workers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import space.dotcat.popularmovies.di.workersInjection.WorkerInjection;
import space.dotcat.popularmovies.repository.MoviesRepository;

public class UpdateMoviesWorker extends Worker {

    public static final String FLAG_KEY = "FLAG";

    private static final String TAG = UpdateMoviesWorker.class.getName();

    @Inject
    MoviesRepository mMoviesRepository;

    public UpdateMoviesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WorkerInjection.inject(this);

        String flag = getInputData().getString(FLAG_KEY);

        Result [] results = new Result[1];

        mMoviesRepository.reloadMoviesWithFlag(flag)
                .doOnSubscribe(subscription -> Log.i(TAG, "Starting updating movies with flag " + flag))
                .subscribe(
                        movies -> {
                            results[0] = Result.SUCCESS;

                            Log.i(TAG, "Successfully reloaded " + movies.size() + " movies with flag " + flag);
                        },

                        throwable -> {
                            results[0] = Result.RETRY;

                            Log.i(TAG, "Error " + throwable.getMessage() +
                                    " has happened during reloading movies with flag" + flag);
                        }
                );

        return results[0];
    }
}
