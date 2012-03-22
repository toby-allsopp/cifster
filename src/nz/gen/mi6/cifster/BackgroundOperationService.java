package nz.gen.mi6.cifster;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import nz.gen.mi6.cifster.operation.NotifiableOperation;

public class BackgroundOperationService extends IntentService {

    private static final String LOG_TAG = BackgroundOperationService.class.getName();

    private static final String EXTRA_NAMESPACE = BackgroundOperationService.class.getPackage().getName();
    public static final String OPERATION_EXTRA = EXTRA_NAMESPACE + ".operation";

    public BackgroundOperationService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final NotifiableOperation operation = intent.getParcelableExtra(OPERATION_EXTRA);
        if (operation == null) {
            Log.e(LOG_TAG, "No operation specified in intent: " + intent);
            return;
        }

        final Pair<Integer, Notification> idAndNotification = operation.createNotification(getApplicationContext());
        startForeground(idAndNotification.first, idAndNotification.second);
        operation.run(getApplicationContext());
        stopForeground(false);
    }
}
