package space.dotcat.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Reviews", foreignKeys = @ForeignKey(entity = Movie.class, parentColumns = "movie_id",
        childColumns = "mMovieId", onDelete = ForeignKey.CASCADE))
public class Review {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "review_id")
    @NonNull
    private String mId;

    @ColumnInfo(name = "mMovieId", index = true)
    private int mMovieId;

    @SerializedName("author")
    @ColumnInfo(name = "review_author")
    private String mAuthor;

    @SerializedName("content")
    @ColumnInfo(name = "review_content")
    private String mContent;

    @Ignore
    public Review() {
    }

    public Review(@NonNull String id, int movieId, String author, String content) {
        mId = id;
        mMovieId = movieId;
        mAuthor = author;
        mContent = content;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        mMovieId = movieId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
