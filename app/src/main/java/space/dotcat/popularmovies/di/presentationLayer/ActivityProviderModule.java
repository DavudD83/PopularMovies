package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import space.dotcat.popularmovies.screen.movies.activity.MoviesActivity;
import space.dotcat.popularmovies.screen.popularMovieDetails.activity.MovieDetailsActivity;

@Module(includes = FragmentProviderModule.class)
public abstract class ActivityProviderModule {

    @ContributesAndroidInjector()
    abstract MoviesActivity provideMainActivity();

    @ContributesAndroidInjector()
    abstract MovieDetailsActivity provideMovieDetailsActivity();
}
