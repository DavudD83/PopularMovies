package space.dotcat.popularmovies.repository.keyValueRepository;

import android.content.SharedPreferences;

import space.dotcat.popularmovies.BuildConfig;
import space.dotcat.popularmovies.R;

public class KeyValueRepositoryImpl implements KeyValueRepository {

    private SharedPreferences mSharedPreferences;

    public KeyValueRepositoryImpl(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public String getStringForKey(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    @Override
    public boolean isSyncNotificationsEnabled() {
        return mSharedPreferences.getBoolean(BuildConfig.PREF_IS_NOTIFICATIONS_ENABLED_KEY,
                BuildConfig.PREF_IS_NOTIFICATIONS_ENABLED_DEFAULT_VALUE);
    }

    @Override
    public long getPeriodOfUpdatingPopularMovies() {
        String value = mSharedPreferences.getString(BuildConfig.PREF_PERIOD_OF_UPDATING_POPULAR_MOVIES_KEY,
                BuildConfig.PREF_PERIOD_OF_UPDATING_POPULAR_MOVIES_DEFAULT);

        return Long.valueOf(value);
    }
}
