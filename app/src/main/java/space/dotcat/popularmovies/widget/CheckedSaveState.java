package space.dotcat.popularmovies.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class CheckedSaveState extends View.BaseSavedState {

    private boolean mIsChecked;

    public static final Creator<CheckedSaveState> CREATOR = new Creator<CheckedSaveState>() {
        @Override
        public CheckedSaveState createFromParcel(Parcel in) {
            return new CheckedSaveState(in);
        }

        @Override
        public CheckedSaveState[] newArray(int size) {
            return new CheckedSaveState[size];
        }
    };

    public CheckedSaveState(Parcel source) {
        super(source);

        mIsChecked = source.readInt() == 1;
    }

    public CheckedSaveState(Parcelable superState) {
        super(superState);
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        int isChecked = mIsChecked ? 1 : 0;

        out.writeInt(isChecked);
    }
}
