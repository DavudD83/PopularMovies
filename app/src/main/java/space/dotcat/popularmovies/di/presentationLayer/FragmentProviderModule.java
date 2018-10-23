package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import space.dotcat.popularmovies.di.presentationLayer.favoriteMovies.FavoriteMoviesFragmentModule;
import space.dotcat.popularmovies.di.presentationLayer.ongoingMovies.OngoingMoviesFragmentModule;
import space.dotcat.popularmovies.di.presentationLayer.popularMovies.PopularMoviesFragmentModule;
import space.dotcat.popularmovies.di.presentationLayer.upcomingMovies.UpcomingMoviesFragmentModule;
import space.dotcat.popularmovies.screen.movies.fragments.favoriteMovies.FavoriteMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.ongoingMovies.OngoingMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.popularMovies.PopularMoviesFragment;
import space.dotcat.popularmovies.screen.movies.fragments.upcomingMovies.UpcomingMoviesFragment;
import space.dotcat.popularmovies.screen.popularMovieDetails.fragments.PopularMovieDetailsFragment;
import space.dotcat.popularmovies.screen.settings.SettingsFragment;

@Module
public abstract class FragmentProviderModule {

    @ContributesAndroidInjector(modules = PopularMoviesFragmentModule.class)
    abstract PopularMoviesFragment providePopularMoviesFragment();

    @ContributesAndroidInjector(modules = OngoingMoviesFragmentModule.class)
    abstract OngoingMoviesFragment provideOngoingMoviesFragment();

    @ContributesAndroidInjector(modules = UpcomingMoviesFragmentModule.class)
    abstract UpcomingMoviesFragment provideUpcomingMoviesFragment();

    @ContributesAndroidInjector(modules = FavoriteMoviesFragmentModule.class)
    abstract FavoriteMoviesFragment provideFavoriteMoviesFragment();

    @ContributesAndroidInjector(modules = PopularMovieDetailsFragmentModule.class)
    abstract PopularMovieDetailsFragment providePopularMoviesDetailsFragment();

    @ContributesAndroidInjector(modules = SettingsFragmentModule.class)
    abstract SettingsFragment provideSettingsFragment();
}
