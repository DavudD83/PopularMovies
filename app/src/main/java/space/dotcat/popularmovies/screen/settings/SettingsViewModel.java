package space.dotcat.popularmovies.screen.settings;

import android.arch.lifecycle.ViewModel;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;

import java.util.HashMap;

import space.dotcat.popularmovies.model.FlexInterval;
import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.utils.updatePeriodCalculator.FlexIntervalCalculator;


public class SettingsViewModel extends ViewModel {

    private KeyValueRepository mKeyValueRepository;

    private FlexIntervalCalculator mFlexIntervalCalculator;

    private Scheduler mScheduler;

    private HashMap<Class<? extends Preference>, EvaluateSummary> mFunctions = new HashMap<>();

    public SettingsViewModel(KeyValueRepository keyValueRepository,
                             FlexIntervalCalculator flexIntervalCalculator, Scheduler scheduler) {
        mKeyValueRepository = keyValueRepository;
        mFlexIntervalCalculator = flexIntervalCalculator;
        mScheduler = scheduler;

        initFunctionsForTypes();
    }

    private void initFunctionsForTypes() {
        mFunctions.put(ListPreference.class, (EvaluateSummary<ListPreference>) preference -> {
            String value = mKeyValueRepository.getStringForKey(preference.getKey(), "");

            int index = preference.findIndexOfValue(value);

            String summary = "";

            if (index >= 0) {
                summary = (String) preference.getEntries()[index];
            }

            return summary;
        });
    }

    public String getValueForPreference(Preference preference) {
        Class<? extends Preference> preferenceClass = preference.getClass();

        if (mFunctions.containsKey(preferenceClass)) {
            return mFunctions.get(preferenceClass).getSummary(preference);
        }

        throw new IllegalArgumentException("You have to program way to get summary for this type of preference "
                + preferenceClass);
    }

    public void replaceUpdatingPopularMoviesWork(String period) {
        long periodInLong = Long.valueOf(period);

        FlexInterval flexInterval = mFlexIntervalCalculator.calculateFlexInterval(periodInLong);

        mScheduler.replaceUpdatingPopularMoviesWork(periodInLong, flexInterval.getFlexInterval(),
                flexInterval.getTimeUnit());
    }

    private interface EvaluateSummary<T extends Preference> {
        String getSummary(T preference);
    }
}
