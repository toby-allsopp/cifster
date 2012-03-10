package nz.gen.mi6.cifster;

import java.util.concurrent.atomic.AtomicInteger;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import nz.gen.mi6.cifster.operation.Operation;

public class BackgroundOperationService extends IntentService {

    private static final String LOG_TAG = BackgroundOperationService.class.getName();
    private static final String EXTRA_NAMESPACE = BackgroundOperationService.class.getPackage().getName();
    public static final String OPERATION_EXTRA = EXTRA_NAMESPACE + ".operation";
    private final AtomicInteger m_nextNotificationId = new AtomicInteger();

    public BackgroundOperationService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final Operation operation = intent.getParcelableExtra(OPERATION_EXTRA);
        if (operation == null) {
            Log.e(LOG_TAG, "No operation specified in intent: " + intent);
            return;
        }

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final int id = m_nextNotificationId.getAndIncrement();
        final CharSequence tickerText = operation.getNotificationText(this);
        final Notification notification = new Notification(
                R.drawable.ic_launcher,
                tickerText,
                System.currentTimeMillis());
        final Intent operationProgressIntent = new Intent(
                this,
                CifsterActivity.class);
        final PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                operationProgressIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(
                this,
                operation.getNotificationTitle(this),
                operation.getNotificationText(this),
                contentIntent);
        this.startForeground(id, notification);
        operation.run();
        notification.setLatestEventInfo(
                this,
                operation.getNotificationTitle(this),
                operation.getNotificationText(this),
                contentIntent);
        notificationManager.notify(id, notification);
        stopForeground(false);
    }
}
