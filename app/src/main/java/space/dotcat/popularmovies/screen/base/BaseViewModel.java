package space.dotcat.popularmovies.screen.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import space.dotcat.popularmovies.model.Error;

public class BaseViewModel extends ViewModel {

    protected MutableLiveData<Error> mError;

    protected MutableLiveData<Boolean> mLoading;

    protected CompositeDisposable mDisposables;

    protected BaseViewModel() {
        mError = new MutableLiveData<>();

        mLoading = new MutableLiveData<>();

        mDisposables = new CompositeDisposable();
    }

    public LiveData<Error> observeErrors() {
        return mError;
    }

    public LiveData<Boolean> observeLoadingIndicator() {
        return mLoading;
    }

    public void resetError() {
        if (mError.getValue() != null) {
            mError.getValue().resetError();
        }
    }

    @Override
    protected void onCleared() {
        mDisposables.clear();
    }
}
