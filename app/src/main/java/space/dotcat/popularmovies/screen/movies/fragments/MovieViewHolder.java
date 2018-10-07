package space.dotcat.popularmovies.screen.movies.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.screen.base.BaseViewHolder;
import space.dotcat.popularmovies.utils.image.ImageLoader;

public class MovieViewHolder extends BaseViewHolder<Movie> {

    @BindView(R.id.iv_movie_poster)
    ImageView mMoviePoster;

    private ImageLoader mImageLoader;

    public MovieViewHolder(View itemView, ImageSize imageSize, ImageLoader imageLoader) {
        super(itemView);

        mImageLoader = imageLoader;

        ViewGroup.LayoutParams layoutParams = mMoviePoster.getLayoutParams();

        layoutParams.width = imageSize.getImageWidth();
        layoutParams.height = imageSize.getImageHeight();

        mMoviePoster.setLayoutParams(layoutParams);
        mMoviePoster.requestLayout();
    }

    @Override
    public void bind(Movie movie) {
        this.itemView.setTag(movie);

        mImageLoader.loadSmallPoster(mMoviePoster, movie.getPoster_path());
    }
}
