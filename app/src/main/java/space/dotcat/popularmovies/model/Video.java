package space.dotcat.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Videos", foreignKeys = @ForeignKey(entity = Movie.class, parentColumns = "movie_id",
        childColumns = "movie_id", onDelete = ForeignKey.CASCADE))
public class Video {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "video_id")
    @NonNull
    private String mId;

    @ColumnInfo(name = "movie_id", index = true)
    private int mMovieId;

    @SerializedName("key")
    @ColumnInfo(name = "video_key")
    private String mKey;

    @SerializedName("type")
    @Ignore
    private String mType;

    @Ignore
    public Video() {
    }

    public Video(@NonNull String id, int movieId, String key) {
        mId = id;
        mMovieId = movieId;
        mKey = key;
    }

    @Ignore
    public Video(@NonNull String id, int movieId, String key, String type) {
        mId = id;
        mMovieId = movieId;
        mKey = key;
        mType = type;
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

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
