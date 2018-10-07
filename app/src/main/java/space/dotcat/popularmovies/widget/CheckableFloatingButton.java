package space.dotcat.popularmovies.widget;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.widget.Checkable;

public class CheckableFloatingButton extends FloatingActionButton implements Checkable {

    private boolean mIsChecked;

    private static final int[] CHECKED_STATE = {
            android.R.attr.state_checked
    };

    public CheckableFloatingButton(Context context) {
        super(context);
    }

    public CheckableFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        if(checked == mIsChecked) {
            return;
        }

        mIsChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }

    @Override
    public boolean performClick() {
        toggle();

        return super.performClick();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE);
        }

        return drawableState;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        CheckedSaveState checkedSaveState = new CheckedSaveState(super.onSaveInstanceState());

        checkedSaveState.setChecked(mIsChecked);

        return checkedSaveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof CheckedSaveState)) {
            super.onRestoreInstanceState(state);

            return;
        }

        CheckedSaveState saveState = (CheckedSaveState) state;

        mIsChecked = saveState.isChecked();

        super.onRestoreInstanceState(saveState.getSuperState());
    }
}
