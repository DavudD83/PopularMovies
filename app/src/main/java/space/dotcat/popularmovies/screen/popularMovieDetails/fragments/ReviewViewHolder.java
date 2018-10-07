package space.dotcat.popularmovies.screen.popularMovieDetails.fragments;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Review;
import space.dotcat.popularmovies.screen.base.BaseViewHolder;

public class ReviewViewHolder extends BaseViewHolder<Review> {

    @BindView(R.id.tv_review_author_name)
    TextView mReviewAuthor;

    @BindView(R.id.tv_review_content)
    TextView mReviewContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Review review) {
        mReviewAuthor.setText(review.getAuthor());

        mReviewContent.setText(review.getContent());
    }
}
