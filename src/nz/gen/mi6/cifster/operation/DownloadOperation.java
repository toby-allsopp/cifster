package nz.gen.mi6.cifster.operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.os.Parcel;
import android.util.Log;
import android.util.Pair;

import nz.gen.mi6.cifster.R;
import nz.gen.mi6.cifster.model.CifsItem;
import nz.gen.mi6.cifster.operation.StreamCopier.Listener;

public class DownloadOperation implements Operation {

    private final CifsItem m_item;
    private final String m_dest;

    public DownloadOperation(final CifsItem item, final String destination) {
        m_item = item;
        m_dest = destination;
    }

    @Override
    public CharSequence getNotificationTitle(final Context context) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CharSequence getNotificationText(final Context context) {
        return context.getString(R.string.COPYING_FROM_TO_NOTIFICATION);
    }

    private long processNode(
            final CifsItem item,
            final File destDir,
            final List<Pair<File, CifsItem>> itemsToCopy) {
        long size = 0;
        switch (item.getType()) {
        case ROOT:
            // Ignore the root dir
            break;
        case WORKGROUP:
        case SERVER:
        case SHARE:
        case DIRECTORY:
            final File subDir = new File(destDir, item.getName());
            itemsToCopy.add(Pair.create(subDir, (CifsItem) null));
            for (final CifsItem child : item.getChildren()) {
                size += processNode(child, subDir, itemsToCopy);
            }
            break;
        case FILE:
            itemsToCopy.add(Pair.create(destDir, item));
            size = item.getSize();
            break;
        }
        return size;
    }

    private class Progress implements Listener {
        private final long mTotalSize;
        private final long mStartTimeNanos;
        private long mCopiedSize = 0;

        public Progress(final long totalSize) {
            mTotalSize = totalSize;
            mStartTimeNanos = System.nanoTime();
        }

        @Override
        public void onProgress(final long numBytes) {
            mCopiedSize += numBytes;
            final double percent = (double) mCopiedSize / mTotalSize * 100;
            final long elapsedTimeNanos = System.nanoTime() - mStartTimeNanos;
            final double speedBytesPerNanosecond = (double) mCopiedSize
                    / elapsedTimeNanos;
            final long ttgNanos = (long) ((mTotalSize - mCopiedSize) / speedBytesPerNanosecond);
            final String msg = String.format(
                    "Copied %d of %d (%.2f%%) @ %.2fkB/s - ETA: %s",
                    mCopiedSize,
                    mTotalSize,
                    percent,
                    speedBytesPerNanosecond * 1000 * 1000,
                    new Date(System.currentTimeMillis() + ttgNanos / 1000
                            / 1000));
            Log.d(LOG_TAG, msg);
        }
    }

    @Override
    public void run() {
        // First, build up the list of files to copy. This allows us to give a
        // percentage complete because we can add up the total size of all the
        // files.
        final List<Pair<File, CifsItem>> itemsToCopy = new ArrayList<Pair<File, CifsItem>>();
        final long totalSize = processNode(
                m_item,
                new File(m_dest),
                itemsToCopy);

        // Then, go through and do the copying.
        final StreamCopier copier = new StreamCopier();
        final Progress progress = new Progress(totalSize);
        for (final Pair<File, CifsItem> dirAndItem : itemsToCopy) {
            final File destDir = dirAndItem.first;
            final CifsItem item = dirAndItem.second;
            if (item == null) {
                Log.i(LOG_TAG, "mkdir: " + destDir);
                destDir.mkdir();
            } else {
                Log.i(LOG_TAG, "copy: " + item + " -> " + destDir);
                final InputStream in = item.getInputStream();
                try {
                    final OutputStream out = new FileOutputStream(new File(
                            destDir,
                            item.getName()));
                    try {
                        copier.copy(in, out, progress);
                    } finally {
                        out.close();
                    }
                } catch (final FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (final IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(m_item, flags);
        dest.writeString(m_dest);
    }

    public static final Creator<DownloadOperation> CREATOR = new Creator<DownloadOperation>() {

        @Override
        public DownloadOperation createFromParcel(final Parcel source) {
            final CifsItem item = source.readParcelable(getClass().getClassLoader());
            final String dest = source.readString();
            return new DownloadOperation(item, dest);
        }

        @Override
        public DownloadOperation[] newArray(final int size) {
            return new DownloadOperation[size];
        }

    };
}
