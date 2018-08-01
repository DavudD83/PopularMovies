package space.dotcat.popularmovies.model;

public class ImageSize {

    private int mImageWidth;

    private int mImageHeight;

    public ImageSize(int screenWidth, int screenHeight) {
        mImageWidth = screenWidth;

        mImageHeight = screenHeight;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    public void setImageWidth(int imageWidth) {
        mImageWidth = imageWidth;
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public void setImageHeight(int imageHeight) {
        mImageHeight = imageHeight;
    }
}
