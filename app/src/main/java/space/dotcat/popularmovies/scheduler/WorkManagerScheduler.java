package space.dotcat.popularmovies.scheduler;

import android.arch.lifecycle.LiveData;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.scheduler.workers.DeleteUnflagedMoviesWorker;
import space.dotcat.popularmovies.scheduler.workers.UpdateMoviesWorker;

public class WorkManagerScheduler implements Scheduler {

    private static final long DELETE_REPEAT_INTERVAL = 1;

    private static final long UPDATE_UPCOMING_REPEAT_INTERVAL = 1;

    private static final long UPDATE_ONGOING_REPEAT_INTERVAL = 1;

    private static final long UPDATE_POPULAR_REPEAT_INTERVAL = 3;

    private static final String DELETE_UNFLAGED_MOVIES_WORK = "DELETING WORK";

    private static final String UPDATE_UPCOMING_MOVIES_WORK = "UPDATING UPCOMING MOVIES WORK";

    private static final String UPDATE_ONGOING_MOVIES_WORK = "UPDATING ONGOING MOVIES WORK";

    private static final String UPDATE_POPULAR_MOVIES_WORK = "UPDATING POPULAR MOVIES WORK";

    private WorkManager mWorkManager;

    @Inject
    public WorkManagerScheduler(WorkManager workManager) {
        mWorkManager = workManager;
    }

    @Override
    public LiveData<WorkStatus> startDeletingUnflagedMovies() {
        Constraints requestConstraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest deleteUnflagedMoviesRequest = new PeriodicWorkRequest.Builder(DeleteUnflagedMoviesWorker.class,
                DELETE_REPEAT_INTERVAL, TimeUnit.DAYS, 20, TimeUnit.HOURS)
                .setConstraints(requestConstraints)
                .build();

        mWorkManager.enqueueUniquePeriodicWork(DELETE_UNFLAGED_MOVIES_WORK, ExistingPeriodicWorkPolicy.KEEP,
                deleteUnflagedMoviesRequest);

        return mWorkManager.getStatusById(deleteUnflagedMoviesRequest.getId());
    }

    @Override
    public LiveData<WorkStatus> startUpdatingUpcomingMovies() {
        PeriodicWorkRequest updateUpcomingMovies = new PeriodicWorkRequest.Builder(UpdateMoviesWorker.class,
                UPDATE_UPCOMING_REPEAT_INTERVAL, TimeUnit.DAYS, 20, TimeUnit.HOURS)
                .setConstraints(createConstraintsForUpdatingWork())
                .setInputData(createInputDataForUpdatingMoviesWithFlag(Movie.FLAG_UPCOMING))
                .build();

        mWorkManager.enqueueUniquePeriodicWork(UPDATE_UPCOMING_MOVIES_WORK, ExistingPeriodicWorkPolicy.KEEP,
                updateUpcomingMovies);

        return mWorkManager.getStatusById(updateUpcomingMovies.getId());
    }

    @Override
    public LiveData<WorkStatus> startUpdatingOngoingMovies() {
        PeriodicWorkRequest updateOngoingMovies = new PeriodicWorkRequest.Builder(UpdateMoviesWorker.class,
                UPDATE_ONGOING_REPEAT_INTERVAL, TimeUnit.DAYS, 20, TimeUnit.HOURS)
                .setConstraints(createConstraintsForUpdatingWork())
                .setInputData(createInputDataForUpdatingMoviesWithFlag(Movie.FLAG_ONGOING))
                .build();

        mWorkManager.enqueueUniquePeriodicWork(UPDATE_ONGOING_MOVIES_WORK, ExistingPeriodicWorkPolicy.KEEP,
                updateOngoingMovies);

        return mWorkManager.getStatusById(updateOngoingMovies.getId());
    }

    @Override
    public LiveData<WorkStatus> startUpdatingPopularMovies() {
        PeriodicWorkRequest updatePopularMovies = new PeriodicWorkRequest.Builder(UpdateMoviesWorker.class,
                UPDATE_POPULAR_REPEAT_INTERVAL, TimeUnit.DAYS, 2, TimeUnit.DAYS)
                .setConstraints(createConstraintsForUpdatingWork())
                .setInputData(createInputDataForUpdatingMoviesWithFlag(Movie.FLAG_POPULAR))
                .build();

        mWorkManager.enqueueUniquePeriodicWork(UPDATE_POPULAR_MOVIES_WORK, ExistingPeriodicWorkPolicy.KEEP,
                updatePopularMovies);

        return mWorkManager.getStatusById(updatePopularMovies.getId());
    }

    private Constraints createConstraintsForUpdatingWork() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
    }

    private Data createInputDataForUpdatingMoviesWithFlag(String flag) {
        return new Data.Builder()
                .putString(UpdateMoviesWorker.FLAG_KEY, flag)
                .build();
    }
}
