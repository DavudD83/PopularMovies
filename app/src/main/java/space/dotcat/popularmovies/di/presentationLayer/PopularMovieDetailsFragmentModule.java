package space.dotcat.popularmovies.di.presentationLayer;

import dagger.Module;
import dagger.Provides;
import space.dotcat.popularmovies.repository.moviesRepository.MoviesRepository;
import space.dotcat.popularmovies.screen.popularMovieDetails.fragments.PopularMovieDetailsFragment;
import space.dotcat.popularmovies.screen.popularMovieDetails.fragments.PopularMovieDetailsViewModelFactory;
import space.dotcat.popularmovies.screen.popularMovieDetails.fragments.ReviewsAdapter;

@Module
public class PopularMovieDetailsFragmentModule {

    @Provides
    PopularMovieDetailsViewModelFactory provideViewModelFactory(PopularMovieDetailsFragment popularMovieDetailsFragment,
                                                                MoviesRepository moviesRepository) {
        int movieId = popularMovieDetailsFragment.getArguments().getInt(PopularMovieDetailsFragment.MOVIE_ID_KEY);

        return new PopularMovieDetailsViewModelFactory(movieId, moviesRepository);
    }

    @Provides
    ReviewsAdapter provideReviewsAdapter() {
        return new ReviewsAdapter();
    }
}
