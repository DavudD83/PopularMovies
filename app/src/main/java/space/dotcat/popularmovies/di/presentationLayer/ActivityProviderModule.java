package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import space.dotcat.popularmovies.screen.movies.activity.MoviesActivity;
import space.dotcat.popularmovies.screen.movieDetails.activity.MovieDetailsActivity;
import space.dotcat.popularmovies.screen.settings.SettingsActivity;

@Module(includes = FragmentProviderModule.class)
public abstract class ActivityProviderModule {

    @ContributesAndroidInjector()
    abstract MoviesActivity provideMainActivity();

    @ContributesAndroidInjector()
    abstract MovieDetailsActivity provideMovieDetailsActivity();

    @ContributesAndroidInjector()
    abstract SettingsActivity provideSettingsActivity();
}
