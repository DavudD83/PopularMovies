package space.dotcat.popularmovies.screen.movieDetails.fragments;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.screen.base.BaseRecyclerAdapter;

public class ReviewsAdapter extends BaseRecyclerAdapter<Review, ReviewViewHolder> {

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.review_item, parent, false);

        ReviewViewHolder reviewViewHolder =  new ReviewViewHolder(view);
        reviewViewHolder.mReviewContent.setOnClickListener(v-> reviewViewHolder.handleClickOnTextView());

        return reviewViewHolder;
    }

    @Override
    protected DiffUtil.Callback createDiffUtilCallback(List<Review> oldItems, List<Review> newItems) {
        return new ReviewsDiffUtilCallback(oldItems, newItems);
    }
}
