package space.dotcat.popularmovies;

import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class KeyValueRepositoryTest {

    private static final String TEST_KEY = "TEST_KEY";

    private static final String TEST_VALUE = "VALUE";

    private static final String DEFAULT_VALUE = "DEFAULT_VALUE";

    private static final boolean IS_NOTIFICATIONS_ENABLED_VALUE = true;

    private static final long PERIOD_OF_UPDATING_POPULAR_MOVIES_VALUE = 7;

    private KeyValueRepository mKeyValueRepository;

    private SharedPreferences mSharedPreferences;

    @Before
    public void init() {
        mKeyValueRepository = AppDelegate.getInstance().getAppLayerComponent().getFakeKeyValueRepository();

        mSharedPreferences = AppDelegate.getInstance().getAppLayerComponent().getFakeSharedPreferences();
    }

    @After
    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.clear();
        editor.apply();
    }

    @Test
    public void testGetValueForKeyWhenValueExist() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(TEST_KEY, TEST_VALUE);
        editor.apply();

        String value = mKeyValueRepository.getStringForKey(TEST_KEY, DEFAULT_VALUE);

        assertEquals(TEST_VALUE, value);
    }

    @Test
    public void testGetValueForKeyWhenNoValue() {
        String value = mKeyValueRepository.getStringForKey(TEST_KEY, DEFAULT_VALUE);

        assertEquals(DEFAULT_VALUE, value);
    }

    @Test
    public void testIsSyncNotificationsEnabled() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putBoolean(BuildConfig.PREF_IS_NOTIFICATIONS_ENABLED_KEY, IS_NOTIFICATIONS_ENABLED_VALUE);
        editor.apply();

        boolean is_notifications_enabled = mKeyValueRepository.isSyncNotificationsEnabled();

        assertEquals(IS_NOTIFICATIONS_ENABLED_VALUE, is_notifications_enabled);
    }

    @Test
    public void testIsSyncNotificationEnabledWhenNoValue() {
        boolean is_notifications_enabled = mKeyValueRepository.isSyncNotificationsEnabled();

        assertEquals(BuildConfig.PREF_IS_NOTIFICATIONS_ENABLED_DEFAULT_VALUE, is_notifications_enabled);
    }

    @Test
    public void testGetPeriodOfUpdatingPopularMovies() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        String period_string = String.valueOf(PERIOD_OF_UPDATING_POPULAR_MOVIES_VALUE);

        editor.putString(BuildConfig.PREF_PERIOD_OF_UPDATING_POPULAR_MOVIES_KEY, period_string);
        editor.apply();

        long period = mKeyValueRepository.getPeriodOfUpdatingPopularMovies();

        assertEquals(PERIOD_OF_UPDATING_POPULAR_MOVIES_VALUE, period);
    }

    @Test
    public void testGetPeriodOfUpdatingWhenNoValue() {
        long period = mKeyValueRepository.getPeriodOfUpdatingPopularMovies();

        assertEquals(BuildConfig.PREF_PERIOD_OF_UPDATING_POPULAR_MOVIES_DEFAULT, String.valueOf(period));
    }
}
