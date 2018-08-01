package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Subcomponent;
import space.dotcat.popularmovies.screen.popularMovieDetails.MovieDetailsActivity;

@Subcomponent(modules = MovieDetailsActivityModule.class)
@ActivityScope
public interface MovieDetailsActivityComponent {

    void inject(MovieDetailsActivity movieDetailsActivity);
}
