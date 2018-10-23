package space.dotcat.popularmovies.utils.bottomNavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class BottomViewBehaviour<V extends View> extends CoordinatorLayout.Behavior<V> {

    public BottomViewBehaviour() {
        super();
    }

    public BottomViewBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull V child, @NonNull View directTargetChild, @NonNull View target,
                                       int axes, int type) {
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            return true;
        }

        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        float verticalPosition = Math.max(0f, Math.min((float) child.getHeight(), child.getTranslationY() + dy));

        child.setTranslationY(verticalPosition);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            if (dependency.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams layoutParams
                        = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();

                layoutParams.setAnchorId(child.getId());
                layoutParams.anchorGravity = Gravity.TOP;
                layoutParams.gravity = Gravity.TOP;
            }
        }

        return super.layoutDependsOn(parent, child, dependency);
    }
}
