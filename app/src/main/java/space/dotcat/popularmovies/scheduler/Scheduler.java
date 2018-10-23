package space.dotcat.popularmovies.scheduler;

import android.arch.lifecycle.LiveData;

import java.util.concurrent.TimeUnit;

import androidx.work.WorkStatus;

public interface Scheduler {

    LiveData<WorkStatus> startDeletingUnflagedMovies();

    LiveData<WorkStatus> startUpdatingUpcomingMovies();

    LiveData<WorkStatus> startUpdatingOngoingMovies();

    LiveData<WorkStatus> startUpdatingPopularMovies(long period_of_updating, long flex_interval,
                                                    TimeUnit flex_time_unit);

    LiveData<WorkStatus> replaceUpdatingPopularMoviesWork(long period_of_updating, long flex_interval,
                                                          TimeUnit flex_time_unit);
}
