package space.dotcat.popularmovies.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import space.dotcat.popularmovies.R;
import space.dotcat.popularmovies.screen.movies.activity.MoviesActivity;

public class NotificationHandlerImpl implements NotificationHandler {

    private static final String MOVIES_UPDATES_CHANNEL = "MOVIES UPDATES CHANNEL ID";

    private static final String MOVIES_UPDATES_CHANNEL_NAME = "Movies updates channel";

    private static final int UpdatedMoviesNotificationId = 10;

    private static final int REQUEST_CODE_PENDING_INTENT = 100;

    private Context mContext;

    private NotificationManager mNotificationManager;

    @Inject
    public NotificationHandlerImpl(Context context, NotificationManager notificationManager) {
        mNotificationManager = notificationManager;

        mContext = context;

        initNotificationChannel();
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel moviesUpdatesChannel = new NotificationChannel(MOVIES_UPDATES_CHANNEL,
                    MOVIES_UPDATES_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            mNotificationManager.createNotificationChannel(moviesUpdatesChannel);
        }
    }

    @Override
    public void sendNewReloadedMoviesNotification() {
        Intent intent = MoviesActivity.getLaunchIntent(mContext);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent launchPopularMoviesFragment = PendingIntent.getActivity(mContext, REQUEST_CODE_PENDING_INTENT,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mContext, MOVIES_UPDATES_CHANNEL)
                .setContentTitle("Movies update")
                .setContentText("Popular movies have been updated. Take a look")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(launchPopularMoviesFragment)
                .setColorized(true)
                .setColor(mContext.getResources().getColor(R.color.colorPrimary))
                .setAutoCancel(true)
                .build();

        mNotificationManager.notify(UpdatedMoviesNotificationId, notification);
    }
}
