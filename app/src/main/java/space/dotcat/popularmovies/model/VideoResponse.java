package space.dotcat.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {

    @SerializedName("id")
    private int mMovieId;

    @SerializedName("results")
    private List<Video> mVideos;

    public VideoResponse(int id, List<Video> videos) {
        mMovieId = id;
        mVideos = videos;
    }

    public int getId() {
        return mMovieId;
    }

    public void setId(int id) {
        mMovieId = id;
    }

    public List<Video> getVideos() {
        for(Video video : mVideos)
            video.setMovieId(mMovieId);

        return mVideos;
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
    }
}
