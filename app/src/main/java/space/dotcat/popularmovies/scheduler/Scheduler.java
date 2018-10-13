package space.dotcat.popularmovies.scheduler;

import android.arch.lifecycle.LiveData;

import androidx.work.WorkStatus;

public interface Scheduler {

    LiveData<WorkStatus> startDeletingUnflagedMovies();

    LiveData<WorkStatus> startUpdatingUpcomingMovies();

    LiveData<WorkStatus> startUpdatingOngoingMovies();

    LiveData<WorkStatus> startUpdatingPopularMovies();
}
