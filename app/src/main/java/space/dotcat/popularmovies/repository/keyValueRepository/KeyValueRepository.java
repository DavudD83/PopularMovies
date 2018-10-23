package space.dotcat.popularmovies.repository.keyValueRepository;

public interface KeyValueRepository {

    String getStringForKey(String key, String defaultValue);

    boolean isSyncNotificationsEnabled();

    long getPeriodOfUpdatingPopularMovies();
}
