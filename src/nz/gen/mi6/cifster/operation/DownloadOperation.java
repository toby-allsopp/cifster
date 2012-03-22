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

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import nz.gen.mi6.cifster.model.CifsItem;

public class DownloadOperation implements Parcelable {

    private static class ItemToCopy {
        public CifsItem m_item;
        public File m_destDir;
        public long m_size;

        public ItemToCopy(
                final CifsItem item,
                final File destDir,
                final long size) {
            super();
            m_item = item;
            m_destDir = destDir;
            m_size = size;
        }
    }

    public interface Listener {
        void onStartPrepare();

        void onFinishPrepare();

        void onStartCopy(long totalNumFiles, long totalSize);

        void onStartFile(CifsItem source, File destination, long fileSize);

        void onProgress(long sizeCopied);

        void onFinishFile();

        void onFinishCopy();
    }

    private static final String LOG_TAG = "DownloadOperation";
    private final CifsItem m_item;
    private final String m_dest;

    public DownloadOperation(final CifsItem item, final String destination) {
        m_item = item;
        m_dest = destination;
    }

    private long processNode(
            final CifsItem item,
            final File destDir,
            final List<ItemToCopy> itemsToCopy) {
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
            itemsToCopy.add(new ItemToCopy(null, subDir, size));
            for (final CifsItem child : item.getChildren()) {
                size += processNode(child, subDir, itemsToCopy);
            }
            break;
        case FILE:
            size = item.getSize();
            itemsToCopy.add(new ItemToCopy(item, destDir, size));
            break;
        }
        return size;
    }

    private class Progress implements StreamCopier.Listener {
        private final long mTotalSize;
        private final long mStartTimeNanos;
        private long mCopiedSize = 0;
        private final Listener mListener;

        public Progress(final long totalSize, final Listener listener) {
            mTotalSize = totalSize;
            mListener = listener;
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
            mListener.onProgress(numBytes);
        }
    }

    public void run(final Listener listener) {
        // First, build up the list of files to copy. This allows us to give a
        // percentage complete because we can add up the total size of all the
        // files.
        listener.onStartPrepare();
        final List<ItemToCopy> itemsToCopy = new ArrayList<ItemToCopy>();
        final long totalSize = processNode(
                m_item,
                new File(m_dest),
                itemsToCopy);
        listener.onFinishPrepare();

        // Then, go through and do the copying.
        final StreamCopier copier = new StreamCopier();
        final Progress progress = new Progress(totalSize, listener);
        listener.onStartCopy(itemsToCopy.size(), totalSize);
        for (final ItemToCopy i : itemsToCopy) {
            if (i.m_item == null) {
                Log.i(LOG_TAG, "mkdir: " + i.m_destDir);
                listener.onStartFile(null, i.m_destDir, i.m_size);
                i.m_destDir.mkdir();
            } else {
                Log.i(LOG_TAG, "copy: " + i.m_item + " -> " + i.m_destDir);
                final File destination = new File(
                        i.m_destDir,
                        i.m_item.getName());
                listener.onStartFile(i.m_item, destination, i.m_size);
                final InputStream in = i.m_item.getInputStream();
                try {
                    final OutputStream out = new FileOutputStream(destination);
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
            listener.onFinishFile();
        }
        listener.onFinishCopy();
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
