package space.dotcat.popularmovies.di.presentationLayer;

import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import space.dotcat.popularmovies.screen.settings.SettingsViewModelFactory;

@Module
public abstract class SettingsFragmentModule {

    @Binds
    abstract ViewModelProvider.Factory provideSettingsViewModelFactory(SettingsViewModelFactory settingsViewModelFactory);
}
