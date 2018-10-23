package space.dotcat.popularmovies.screen.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import space.dotcat.popularmovies.R;

public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private static final String TAG = BaseActivity.class.getName();

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    private Unbinder mUnbinder;

    protected FragmentManager mFragmentManager;

    @Nullable
    protected Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mUnbinder = ButterKnife.bind(this);

        onSetupActionBar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }

    protected void onSetupActionBar() {
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

    protected void addFragment(int containerId, Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in,
                        android.R.anim.fade_out)
//                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .commit();
    }
}
