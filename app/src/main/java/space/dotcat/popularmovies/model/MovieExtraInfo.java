package space.dotcat.popularmovies.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

public class MovieExtraInfo {

//    @Embedded
    public Video mTrailer;

//    @Relation(entity = Review.class, entityColumn = "mMovieId", parentColumn = "movie_id")
    public List<Review> mReviewList;

    public MovieExtraInfo(Video trailer) {
        mTrailer = trailer;
    }

    @Ignore
    public MovieExtraInfo(Video trailer, List<Review> reviewList) {
        mTrailer = trailer;
        mReviewList = reviewList;
    }

    public Video getTrailer() {
        return mTrailer;
    }

    public void setTrailer(Video trailer) {
        mTrailer = trailer;
    }

    public List<Review> getReviewList() {
        return mReviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        mReviewList = reviewList;
    }
}
