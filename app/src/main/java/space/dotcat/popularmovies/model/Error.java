package space.dotcat.popularmovies.model;

public class Error {

    public static int UNKNOWN_ERROR = 0;

    private Throwable mThrowable;

    private int mErrorCode;

    private Error(Throwable throwable) {
        mThrowable = throwable;
    }

    private Error(Throwable throwable, int errorCode) {
        mThrowable = throwable;

        mErrorCode = errorCode;
    }

    public static Error create(Throwable throwable) {
        return new Error(throwable, UNKNOWN_ERROR);
    }

    public static Error create(Throwable throwable, int errorCode) {
        return new Error(throwable, errorCode);
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setThrowable(Throwable throwable) {
        mThrowable = throwable;
    }

    public boolean isExist() {
        return mThrowable != null;
    }

    public void resetError() {
        mThrowable = null;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }
}
