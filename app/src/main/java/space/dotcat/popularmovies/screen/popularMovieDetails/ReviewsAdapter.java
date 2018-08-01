package space.dotcat.popularmovies.screen.popularMovieDetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private List<Review> mReviews;

    public ReviewsAdapter() {
        mReviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.review_item, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);

        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void updateReviews(List<Review> reviewList) {
        mReviews = null;

        mReviews = reviewList;

        notifyDataSetChanged();
    }
}
