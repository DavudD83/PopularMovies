package space.dotcat.popularmovies.screen.movies.fragments;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.ImageSize;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.screen.base.BaseRecyclerAdapter;
import space.dotcat.popularmovies.utils.image.ImageLoader;

public class MoviesAdapter extends BaseRecyclerAdapter<Movie, MovieViewHolder> {

    private ImageLoader mImageLoader;

    private ImageSize mImageSize;

    private OnMovieClickListener mOnMovieClickListener;

    public MoviesAdapter(ImageLoader imageLoader, ImageSize imageSize, OnMovieClickListener onMovieClickListener) {
        super();

        mImageLoader = imageLoader;

        mImageSize = imageSize;

        mOnMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);

        MovieViewHolder movieViewHolder = new MovieViewHolder(view, mImageSize, mImageLoader);

        movieViewHolder.itemView.setOnClickListener(v-> {
            Movie movie = (Movie) v.getTag();

            int movieId = movie.getId();
            ImageView moviePoster = movieViewHolder.mMoviePoster;

            mOnMovieClickListener.onMovieClick(movieId, moviePoster);
        });

        return movieViewHolder;
    }

    @Override
    protected DiffUtil.Callback createDiffUtilCallback(List<Movie> oldItems, List<Movie> newItems) {
        return new MoviesDiffUtilCallback(oldItems, newItems);
    }

    public interface OnMovieClickListener {
        void onMovieClick(int movieId, ImageView moviePoster);
    }
}
