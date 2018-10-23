package space.dotcat.popularmovies.screen.settings;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import space.dotcat.popularmovies.repository.keyValueRepository.KeyValueRepository;
import space.dotcat.popularmovies.scheduler.Scheduler;
import space.dotcat.popularmovies.utils.updatePeriodCalculator.FlexIntervalCalculator;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private KeyValueRepository mKeyValueRepository;

    private FlexIntervalCalculator mFlexIntervalCalculator;

    private Scheduler mScheduler;

    @Inject
    public SettingsViewModelFactory(KeyValueRepository keyValueRepository,
                                    FlexIntervalCalculator flexIntervalCalculator, Scheduler scheduler) {
        mKeyValueRepository = keyValueRepository;
        mFlexIntervalCalculator = flexIntervalCalculator;
        mScheduler = scheduler;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SettingsViewModel(mKeyValueRepository, mFlexIntervalCalculator, mScheduler);
    }
}
