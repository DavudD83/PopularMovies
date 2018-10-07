package space.dotcat.popularmovies.screen.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import space.dotcat.popularmovies.R;

public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment {

    protected VM mViewModel;

    private Snackbar mSnackbar;

    @Nullable
    private ProgressBar mProgressBar;

    private boolean mHasProgressIndicator = false;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = createViewModel();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mHasProgressIndicator) {
            mViewModel.observeLoadingIndicator().observe(this, isLoading-> {
                if (isLoading) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    protected abstract VM createViewModel();

    protected abstract View getContainerForSnackbar();

    protected void setHasProgressIndicator() {
        mHasProgressIndicator = true;
    }

    protected void showError(String errorMessage, View.OnClickListener errorHandler ) {
        mSnackbar = createSnackbar(errorMessage, Snackbar.LENGTH_INDEFINITE);

        if (errorHandler == null) {
            mSnackbar.setAction("Dismiss", handler -> {
                mViewModel.resetError();

                mSnackbar.dismiss();
            });
        } else {
            mSnackbar.setAction("Try again", errorHandler);
        }

        mSnackbar.show();
    }

    protected void showMessage(String message) {
        mSnackbar = createSnackbar(message, Snackbar.LENGTH_SHORT);

        mSnackbar.show();
    }

    public void resetError() {
        if (mSnackbar == null) {
            return;
        }

        mSnackbar.dismiss();

        mViewModel.resetError();
    }

    protected Snackbar getSnackbar() {
        return mSnackbar;
    }

    protected void setupLoadingIndicator(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    private Snackbar createSnackbar(String message, int snackbarDuration) {
        View container = getContainerForSnackbar();

        return Snackbar.make(container, message, snackbarDuration);
    }
}
