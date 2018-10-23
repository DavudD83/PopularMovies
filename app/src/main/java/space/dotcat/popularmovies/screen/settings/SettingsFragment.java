package space.dotcat.popularmovies.screen.settings;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import space.dotcat.popularmovies.BuildConfig;
import space.dotcat.popularmovies.R;


public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    ViewModelProvider.Factory mFactory;

    private SettingsViewModel mSettingsViewModel;

    public SettingsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, false);

        mSettingsViewModel = ViewModelProviders.of(this, mFactory).get(SettingsViewModel.class);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int countOfPreferences = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < countOfPreferences; i++) {
            Preference preference = preferenceScreen.getPreference(i);

            if(preference instanceof CheckBoxPreference) {
                continue;
            }

            String summary = mSettingsViewModel.getValueForPreference(preference);

            setSummary(preference, summary);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference instanceof CheckBoxPreference) {
            return;
        }

        String currentValue = mSettingsViewModel.getValueForPreference(preference);

        setSummary(preference, currentValue);

        if (key.equals(BuildConfig.PREF_PERIOD_OF_UPDATING_POPULAR_MOVIES_KEY)) {
            mSettingsViewModel.replaceUpdatingPopularMoviesWork(currentValue);
        }
    }

    private void setSummary(Preference preference, String summary) {
        preference.setSummary(summary);
    }
}
