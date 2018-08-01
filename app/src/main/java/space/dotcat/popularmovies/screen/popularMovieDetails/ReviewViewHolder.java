package space.dotcat.popularmovies.screen.popularMovieDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.model.Review;

public class ReviewViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tv_review_author_name)
    TextView mReviewAuthor;

    @BindView(R.id.tv_review_content)
    TextView mReviewContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(Review review) {
        mReviewAuthor.setText(review.getAuthor());

        mReviewContent.setText(review.getContent());
    }
}
