package space.dotcat.popularmovies.utils.image;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.utils.image.ImageLoader;


public class PicassoImageLoader implements ImageLoader {

    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";

    private static final String LARGE_POSTER_SIZE = "w780/";

    private static final String SMALL_POSTER_SIZE = "w185/";

    @Inject
    public PicassoImageLoader() {
    }

    @Override
    public void loadSmallPoster(ImageView imageView, String url) {
        String loadingUrl = BASE_POSTER_URL + SMALL_POSTER_SIZE + url;

        loadImage(imageView, loadingUrl);
    }

    @Override
    public void loadLargePoster(ImageView imageView, String url) {
        String loadingUrl = BASE_POSTER_URL + LARGE_POSTER_SIZE + url;

        loadImage(imageView, loadingUrl);
    }

    private void loadImage(ImageView imageView, String loadingUrl) {
        Picasso.get()
                .load(loadingUrl)
                .placeholder(R.drawable.movie_placeholder)
                .noFade()
                .into(imageView);
    }
}
