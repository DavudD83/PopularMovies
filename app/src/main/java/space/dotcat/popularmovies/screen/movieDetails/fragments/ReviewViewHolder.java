package space.dotcat.popularmovies.screen.movieDetails.fragments;

import android.text.TextUtils;
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
    public TextView mReviewContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Review review) {
        mReviewAuthor.setText(review.getAuthor());

        mReviewContent.setText(review.getContent());
    }

    public void handleClickOnTextView() {
        int currentLineCount = mReviewContent.getMaxLines();

        if (currentLineCount == 3) {
            mReviewContent.setMaxLines(Integer.MAX_VALUE);
            mReviewContent.setEllipsize(null);
        } else {
            mReviewContent.setEllipsize(TextUtils.TruncateAt.END);
            mReviewContent.setMaxLines(3);
        }
    }
}
