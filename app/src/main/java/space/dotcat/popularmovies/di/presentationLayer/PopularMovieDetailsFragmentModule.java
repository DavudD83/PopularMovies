package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.screen.movieDetails.fragments.MovieDetailsFragment;
import space.dotcat.popularmovies.screen.movieDetails.fragments.MovieDetailsViewModelFactory;
import space.dotcat.popularmovies.screen.movieDetails.fragments.ReviewsAdapter;

@Module
public class PopularMovieDetailsFragmentModule {

    @Provides
    MovieDetailsViewModelFactory provideViewModelFactory(MovieDetailsFragment popularMovieDetailsFragment,
                                                         MoviesRepository moviesRepository) {
        int movieId = popularMovieDetailsFragment.getArguments().getInt(MovieDetailsFragment.EXTRA_MOVIE_ID_KEY);

        return new MovieDetailsViewModelFactory(movieId, moviesRepository);
    }

    @Provides
    ReviewsAdapter provideReviewsAdapter() {
        return new ReviewsAdapter();
    }
}
