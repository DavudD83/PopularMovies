package space.dotcat.popularmovies.di.appLayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {

    private static final String PREF_NAME_TEST = "PREF_TEST";

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME_TEST, Context.MODE_PRIVATE);
    }
}
