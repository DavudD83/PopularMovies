package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Subcomponent;
import space.dotcat.popularmovies.screen.popularMovies.PopularMoviesActivity;

@Subcomponent(modules = {PopularMoviesActivityModule.class})
@ActivityScope
public interface PopularMoviesActivityComponent {

        void inject(PopularMoviesActivity popularMoviesActivity);
}
