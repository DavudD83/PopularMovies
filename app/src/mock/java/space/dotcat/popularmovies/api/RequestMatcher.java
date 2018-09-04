package space.dotcat.popularmovies.api;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class RequestMatcher {

    private static final String POPULAR_MOVIES = "popular";

    private static final String VIDEOS = "videos";

    private static final String REVIEWS = "reviews";

    private static final MediaType MEDIA_TYPE = MediaType.parse("Application/json");

    private HashMap<String, String> mAddresses = new HashMap<>();

    private Context mContext;

    public RequestMatcher(Context context) {
        mContext = context;

        mAddresses.put(POPULAR_MOVIES, "popularMovies.json");
        mAddresses.put(VIDEOS, "videos.json");
        mAddresses.put(REVIEWS, "reviews.json");
    }

    public boolean isMatch(String request) {
        return mAddresses.containsKey(request);
    }

    public Response getResponseForRequest(String requestPath, Request request) {
        String assetName = mAddresses.get(requestPath);

        byte [] data = new byte[8192];

        try {
            mContext.getAssets().open(assetName).read(data);
        } catch (IOException e) {
            throw new IllegalStateException("You have not created mock json file for request " + request);
        }

        Response.Builder builder = new Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MEDIA_TYPE, data));

        return builder.build();
    }
}
