package space.dotcat.popularmovies.utils.image;

import android.widget.ImageView;

public interface ImageLoader {

    void loadSmallPoster(ImageView imageView, String url);

    void loadLargePoster(ImageView imageView, String url);
}
