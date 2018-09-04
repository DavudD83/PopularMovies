package space.dotcat.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Movies")
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "movie_id")
    private int mId;

    @SerializedName("vote_average")
    @ColumnInfo(name = "movie_average_rating")
    private float mVote_average;

    @SerializedName("title")
    @ColumnInfo(name = "movie_title")
    private String mTitle;

    @SerializedName("popularity")
    @ColumnInfo(name = "movie_popularity")
    private float mPopularity;

    @SerializedName("poster_path")
    @ColumnInfo(name = "movie_poster_path")
    private String mPoster_path;

    @SerializedName("original_language")
    @ColumnInfo(name = "movie_original_language")
    private String mOriginal_language;

    @SerializedName("overview")
    @ColumnInfo(name = "movie_overview")
    private String mOverview;

    @SerializedName("release_date")
    @ColumnInfo(name = "movie_release_date")
    private String mReleaseDate;

    @ColumnInfo(name = "movie_is_favorite")
    private boolean mIsFavorite;

    @Ignore
    public Movie() {
    }

    public Movie(int id, float vote_average, String title, float popularity, String poster_path,
                 String original_language, String overview, String releaseDate) {
        mId = id;
        mVote_average = vote_average;
        mTitle = title;
        mPopularity = popularity;
        mPoster_path = poster_path;
        mOriginal_language = original_language;
        mOverview = overview;
        mReleaseDate = releaseDate;

        mIsFavorite = false; //by default favorite is false but can be changed later by the user wish
    }

    @Ignore
    protected Movie(Parcel in) {
        mId = in.readInt();
        mVote_average = in.readFloat();
        mTitle = in.readString();
        mPopularity = in.readFloat();
        mPoster_path = in.readString();
        mOriginal_language = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public float getVote_average() {
        return mVote_average;
    }

    public void setVote_average(float vote_average) {
        mVote_average = vote_average;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    public String getPoster_path() {
        return mPoster_path;
    }

    public void setPoster_path(String poster_path) {
        mPoster_path = poster_path;
    }

    public String getOriginal_language() {
        return mOriginal_language;
    }

    public void setOriginal_language(String original_language) {
        mOriginal_language = original_language;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeFloat(mVote_average);
        dest.writeString(mTitle);
        dest.writeFloat(mPopularity);
        dest.writeString(mPoster_path);
        dest.writeString(mOriginal_language);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }
}
