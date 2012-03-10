package nz.gen.mi6.cifster.operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.os.Parcel;

import nz.gen.mi6.cifster.R;
import nz.gen.mi6.cifster.model.CifsItem;

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

    @Override
    public void run() {
        final StreamCopier copier = new StreamCopier();
        final Queue<CifsItem> items = new LinkedList<CifsItem>();
        items.add(m_item);
        CifsItem item;
        File destDir = new File(m_dest);
        while ((item = items.poll()) != null) {
            switch (item.getType()) {
            case ROOT:
                // Ignore the root dir
                break;
            case WORKGROUP:
            case SERVER:
            case SHARE:
            case DIRECTORY:
                destDir = new File(destDir, item.getName());
                destDir.mkdir();
                items.addAll(item.getChildren());
                break;
            case FILE:
                final InputStream in = item.getInputStream();
                try {
                    final OutputStream out = new FileOutputStream(new File(
                            destDir,
                            item.getName()));
                    try {
                        copier.copy(in, out);
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
                break;
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
