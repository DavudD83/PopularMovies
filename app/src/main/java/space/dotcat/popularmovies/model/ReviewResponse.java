package space.dotcat.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse {

    @SerializedName("id")
    public int mId;

    @SerializedName("results")
    public List<Review> mReviews;

    public ReviewResponse(int id, List<Review> reviews) {
        mId = id;
        mReviews = reviews;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public List<Review> getReviews() {
        for(Review review : mReviews)
            review.setMovieId(mId);

        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }
}
