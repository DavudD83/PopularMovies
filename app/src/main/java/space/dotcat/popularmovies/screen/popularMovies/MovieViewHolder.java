package space.dotcat.popularmovies.screen.popularMovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.ImageSize;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_movie_poster)
    ImageView mMoviePoster;

    public MovieViewHolder(View itemView, ImageSize imageSize) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        ViewGroup.LayoutParams layoutParams = mMoviePoster.getLayoutParams();

        layoutParams.width = imageSize.getImageWidth();
        layoutParams.height = imageSize.getImageHeight();

        mMoviePoster.setLayoutParams(layoutParams);
        mMoviePoster.requestLayout();
    }

    public void bind(Movie movie) {
        String url = "https://image.tmdb.org/t/p/w185/" + movie.getPoster_path();

        Picasso.get()
                .load(url)
                .noFade()
                .into(mMoviePoster);
    }
}
