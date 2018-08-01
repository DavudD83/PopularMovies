package space.dotcat.popularmovies.screen.base;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getName();

    private Snackbar mSnackbar;

    private View.OnClickListener mErrorHandler;

    @Nullable
    protected Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);

        initDependencyGraph();

        setupActionBar();
    }

    protected void setupActionBar() {
        if(mToolbar == null) {
            Log.d(TAG, "Toolbar is null. Can not set action bar");
            return;
        }

        setSupportActionBar(mToolbar);
    }

    protected void setToolbar(Toolbar toolbar) {
        if(toolbar == null) {
            Log.d(TAG, "Toolbar is null. Can not set toolbar");
            return;
        }

        mToolbar = toolbar;
    }

    protected abstract void initDependencyGraph();

    protected void showError(Throwable throwable, View view, View.OnClickListener errorHandler) {
        mSnackbar = Snackbar.make(view, throwable.getMessage(), Snackbar.LENGTH_INDEFINITE);

        if (errorHandler == null) {
            mSnackbar.setAction("Dismiss", v-> mSnackbar.dismiss());
        } else {
            mSnackbar.setAction("Try again", errorHandler);
        }

        mSnackbar.show();
//        if(mSnackbar == null) {
//            mSnackbar = Snackbar.make(view, throwable.getMessage(),
//                    Snackbar.LENGTH_INDEFINITE);
//
//            if(errorHandler == null) {
//                mSnackbar.setAction("Dismiss", v -> mSnackbar.dismiss());
//            } else {
//                mSnackbar.setAction("Try again", errorHandler);
//            }
//        }
//
//        mSnackbar.show();
    }

    protected void setErrorHandler(@StringRes int textId, View.OnClickListener handler) {
        if(mSnackbar == null) {
            return;
        }

        mSnackbar.setAction(textId, handler);
    }

    protected Snackbar getSnackbar() {
        return mSnackbar;
    }
}
