package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.di.appLayer.qualifiers.Main;
import space.dotcat.popularmovies.repository.MoviesRepository;
import space.dotcat.popularmovies.screen.popularMovieDetails.MovieDetailsViewModelFactory;
import space.dotcat.popularmovies.screen.popularMovieDetails.ReviewsAdapter;

@Module
public class MovieDetailsActivityModule {

    private int mMovieId;

    public MovieDetailsActivityModule(int movieId) {
        mMovieId = movieId;
    }

    @Provides
    @ActivityScope
    MovieDetailsViewModelFactory provideViewModelFactory(@Main MoviesRepository moviesRepository) {
        return new MovieDetailsViewModelFactory(mMovieId, moviesRepository);
    }

    @Provides
    @ActivityScope
    ReviewsAdapter provideReviewsAdapter() {
        return new ReviewsAdapter();
    }
}
