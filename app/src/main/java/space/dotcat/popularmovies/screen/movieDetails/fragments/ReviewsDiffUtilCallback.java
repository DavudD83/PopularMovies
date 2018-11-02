package space.dotcat.popularmovies.screen.movieDetails.fragments;

import java.util.List;

import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.screen.base.BaseDiffUtilCallback;

public class ReviewsDiffUtilCallback extends BaseDiffUtilCallback<Review> {

    public ReviewsDiffUtilCallback(List<Review> oldItems, List<Review> newItems) {
        super(oldItems, newItems);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Review oldReview = getOldItem(oldItemPosition);

        Review newReview = getNewItem(newItemPosition);

        return oldReview.getId().equals(newReview.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Review oldReview = getOldItem(oldItemPosition);

        Review newReview = getNewItem(newItemPosition);

        return oldReview.getContent().length() == newReview.getContent().length(); // TODO
    }
}
