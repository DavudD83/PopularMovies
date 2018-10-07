package space.dotcat.popularmovies.screen.base;

import android.support.v7.util.DiffUtil;

import java.util.List;


public abstract class BaseDiffUtilCallback<T> extends DiffUtil.Callback {

    protected List<T> mOldItems;

    protected List<T> mNewItems;

    public BaseDiffUtilCallback(List<T> oldItems, List<T> newItems) {
        mOldItems = oldItems;
        mNewItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return mOldItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewItems.size();
    }

    protected T getOldItem(int position) {
        return mOldItems.get(position);
    }

    protected T getNewItem(int position) {
        return mNewItems.get(position);
    }
}
