package space.dotcat.popularmovies.scheduler.workers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import space.dotcat.popularmovies.di.workersInjection.WorkerInjection;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import timber.log.Timber;


public class DeleteUnflagedMoviesWorker extends Worker {

    @Inject
    MoviesRepository mMoviesRepository;

    public DeleteUnflagedMoviesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WorkerInjection.inject(this);

        Timber.i("Starting deleting movies without flag");

        int numberOfDeletedMovies = mMoviesRepository.deleteMoviesWithoutFlags();

        Timber.i("%s movies has been deleted by worker.", numberOfDeletedMovies);

        return Result.SUCCESS;
    }

}
