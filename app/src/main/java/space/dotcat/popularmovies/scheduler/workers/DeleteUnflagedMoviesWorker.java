package space.dotcat.popularmovies.scheduler.workers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import space.dotcat.popularmovies.di.workersInjection.WorkerInjection;
import space.dotcat.popularmovies.repository.MoviesRepository;


public class DeleteUnflagedMoviesWorker extends Worker {

    private static final String TAG = DeleteUnflagedMoviesWorker.class.getName();

    @Inject
    MoviesRepository mMoviesRepository;

    public DeleteUnflagedMoviesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WorkerInjection.inject(this);

        Log.i(TAG, "Starting deleting movies without flag");

        int numberOfDeletedMovies = mMoviesRepository.deleteMoviesWithoutFlags();

        Log.i(TAG, numberOfDeletedMovies + " movies has been deleted by worker.");

        return Result.SUCCESS;
    }

}
