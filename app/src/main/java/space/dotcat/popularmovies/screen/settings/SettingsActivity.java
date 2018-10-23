package space.dotcat.popularmovies.screen.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.base.BaseActivity;


public class SettingsActivity extends BaseActivity {

    @BindView(R.id.tb_app_toolbar)
    Toolbar mToolbar;

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onSetupActionBar() {
        mToolbar.setTitle(R.string.settings_screen_title);

        setToolbar(mToolbar);

        super.onSetupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();

            return true;
        }

        return false;
    }
}
