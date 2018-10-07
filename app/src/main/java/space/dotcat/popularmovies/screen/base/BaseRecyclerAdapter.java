package space.dotcat.popularmovies.screen.base;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

    protected List<T> mItems;

    protected OnItemClickListener<T> mOnItemClickListener;

    protected View.OnClickListener mStandartClickListener = v-> {
        T item = (T) v.getTag();

        mOnItemClickListener.onItemClick(item);
    };

    public BaseRecyclerAdapter() {
        mItems = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = mItems.get(position);

        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void changeData(List<T> newData) {
        DiffUtil.Callback callback = createDiffUtilCallback(mItems, newData);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        mItems.clear();
        mItems.addAll(newData);

        diffResult.dispatchUpdatesTo(this);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected abstract DiffUtil.Callback createDiffUtilCallback(List<T> oldItems, List<T> newItems);

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }
}
