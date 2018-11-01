package space.dotcat.popularmovies.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestMatcher {

    public static final String IS_ERROR = "IS_ERROR";

    public static final String ERROR_MESSAGE = "Internet connection error";

    private static final String POPULAR_MOVIES = "movie";

    private static final String VIDEOS = "videos";

    private static final String REVIEWS = "reviews";

    private static final MediaType MEDIA_TYPE = MediaType.parse("Application/json");

    private HashMap<String, String> mAddresses = new HashMap<>();

    private Context mContext;

    private SharedPreferences mSharedPreferences;

    public RequestMatcher(Context context, SharedPreferences sharedPreferences) {
        mContext = context;
        mSharedPreferences = sharedPreferences;

        mAddresses.put(POPULAR_MOVIES, "movies.json");
        mAddresses.put(VIDEOS, "videos.json");
        mAddresses.put(REVIEWS, "reviews.json");
    }

    public boolean isMatch(String request) {
        return mAddresses.containsKey(request);
    }

    public Response getResponseForRequest(String requestPath, Request request) {
        boolean is_error = mSharedPreferences.getBoolean(IS_ERROR, false);

        if (is_error) {
            Response.Builder builder = new Response.Builder()
                    .code(500)
                    .message(ERROR_MESSAGE)
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MEDIA_TYPE, new byte[8192]))
                    .request(request);

            return builder.build();
        }

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
