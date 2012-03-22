package nz.gen.mi6.cifster.operation;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Pair;
import android.widget.RemoteViews;

import nz.gen.mi6.cifster.CifsterActivity;
import nz.gen.mi6.cifster.R;
import nz.gen.mi6.cifster.model.CifsItem;

public class NotfiableDownloadOperation implements NotifiableOperation {

    private static final String LOG_TAG = "DownloadOperation";
    private static final AtomicInteger m_nextNotificationId = new AtomicInteger();

    private final DownloadOperation m_operation;
    private final Notification m_notification = new Notification();
    private final int m_id = m_nextNotificationId.getAndIncrement();

    public NotfiableDownloadOperation(final DownloadOperation operation) {
        m_operation = operation;
    }

    @Override
    public Pair<Integer, Notification> createNotification(final Context context) {
        m_notification.icon = R.drawable.ic_launcher;
        m_notification.tickerText = context.getString(R.string.STARTING_DOWNLOAD_NOTIFICATION_TICKER);
        m_notification.when = System.currentTimeMillis();
        final Intent operationProgressIntent = new Intent(
                context,
                CifsterActivity.class);
        m_notification.contentIntent = PendingIntent.getActivity(
                context,
                0,
                operationProgressIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        final RemoteViews cv = new RemoteViews(
                context.getPackageName(),
                R.layout.download_notification);
        m_notification.contentView = cv;
        return Pair.create(m_id, m_notification);
    }

    @Override
    public void run(final Context context) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        m_operation.run(new DownloadOperation.Listener() {

            private static final int PROGRESS_SCALE = 1000;
            private String m_fileName;
            private long m_fileSize;
            private long m_fileCopied;
            private long m_totalCopied;
            private long m_totalSize;
            private long m_lastNotificationUpdateTimeNanos;

            @Override
            public void onStartPrepare() {
                final RemoteViews cv = m_notification.contentView;
                cv.setTextViewText(
                        R.id.fileText,
                        context.getText(R.string.PREPARING_NOTIFICATION));
                updateNotification();
            }

            private void updateNotification() {
                if (m_lastNotificationUpdateTimeNanos < System.nanoTime()
                        - TimeUnit.SECONDS.toNanos(1)) {
                    final RemoteViews cv = m_notification.contentView;
                    if (m_totalSize == 0) {
                        cv.setProgressBar(R.id.overallProgressBar, 0, 0, true);
                    } else {
                        cv.setProgressBar(
                                R.id.overallProgressBar,
                                PROGRESS_SCALE,
                                Math.round((float) m_totalCopied / m_totalSize
                                        * PROGRESS_SCALE),
                                false);
                    }
                    if (m_fileSize == 0) {
                        cv.setProgressBar(R.id.fileProgressBar, 0, 0, true);
                    } else {
                        cv.setProgressBar(
                                R.id.fileProgressBar,
                                PROGRESS_SCALE,
                                Math.round((float) m_fileCopied / m_fileSize
                                        * PROGRESS_SCALE),
                                false);
                    }
                    notificationManager.notify(m_id, m_notification);
                    m_lastNotificationUpdateTimeNanos = System.nanoTime();
                }
            }

            @Override
            public void onFinishPrepare() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartCopy(
                    final long totalNumFiles,
                    final long totalSize) {
                m_totalSize = totalSize;
            }

            @Override
            public void onStartFile(
                    final CifsItem source,
                    final File destination,
                    final long fileSize) {
                m_fileName = destination.getName();
                m_fileSize = fileSize;
                m_fileCopied = 0;
                final RemoteViews cv = m_notification.contentView;
                cv.setTextViewText(R.id.fileText, m_fileName);
                cv.setProgressBar(
                        R.id.fileProgressBar,
                        PROGRESS_SCALE,
                        0,
                        false);
                updateNotification();
            }

            @Override
            public void onProgress(final long sizeCopied) {
                m_fileCopied += sizeCopied;
                m_totalCopied += sizeCopied;
                updateNotification();
            }

            @Override
            public void onFinishFile() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinishCopy() {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(m_operation, flags);
    }

    public static final Creator<NotfiableDownloadOperation> CREATOR = new Creator<NotfiableDownloadOperation>() {

        @Override
        public NotfiableDownloadOperation createFromParcel(final Parcel source) {
            final DownloadOperation operation = source.readParcelable(getClass().getClassLoader());
            return new NotfiableDownloadOperation(operation);
        }

        @Override
        public NotfiableDownloadOperation[] newArray(final int size) {
            return new NotfiableDownloadOperation[size];
        }

    };
}
