package space.dotcat.popularmovies.screen.popularMovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Movie;
import space.dotcat.popularmovies.model.ImageSize;

public class PopularMoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private ImageSize mImageSize;

    private List<Movie> mMovieList;

    private OnMovieClickListener mOnMovieClickListener;

    private View.OnClickListener mOnClickListener = view -> {
        Movie movie = (Movie) view.getTag();

        int movieId = movie.getId();

        mOnMovieClickListener.onMovieClick(movieId);
    };

    public PopularMoviesAdapter(ImageSize imageSize, OnMovieClickListener onMovieClickListener) {
        mImageSize = imageSize;
        mOnMovieClickListener = onMovieClickListener;

        mMovieList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(view, mImageSize);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        holder.itemView.setTag(movie);
        holder.itemView.setOnClickListener(mOnClickListener);

        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void updateMovies(List<Movie> movies) {
        mMovieList = null;
        mMovieList = movies;

        notifyDataSetChanged();
    }

    public interface OnMovieClickListener {
        void onMovieClick(int movieId);
    }
}
