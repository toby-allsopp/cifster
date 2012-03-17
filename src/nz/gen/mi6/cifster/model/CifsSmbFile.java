package nz.gen.mi6.cifster.model;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

class CifsSmbFile implements CifsItem {

    private static final String LOG_TAG = "CifsSmbFile";

    private final SmbFile m_smb_file;

    public CifsSmbFile() {
        try {
            m_smb_file = new SmbFile("smb://");
        } catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public CifsSmbFile(final SmbFile smb_file) {
        m_smb_file = smb_file;
    }

    @Override
    public String getName() {
        return m_smb_file.getName();
    }

    @Override
    public Type getType() {
        try {
            switch (m_smb_file.getType()) {
            case SmbFile.TYPE_WORKGROUP:
                return Type.WORKGROUP;
            case SmbFile.TYPE_SERVER:
                return Type.SERVER;
            case SmbFile.TYPE_SHARE:
                return Type.SHARE;
            case SmbFile.TYPE_FILESYSTEM:
            case SmbFile.TYPE_COMM:
            case SmbFile.TYPE_NAMED_PIPE:
            case SmbFile.TYPE_PRINTER:
                try {
                    if (m_smb_file.isDirectory()) {
                        return Type.DIRECTORY;
                    } else {
                        return Type.FILE;
                    }
                } catch (final SmbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (final SmbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Type.FILE;
    }

    @Override
    public List<CifsItem> getChildren() {
        final List<CifsItem> children = new ArrayList<CifsItem>();
        try {
            for (final SmbFile file : m_smb_file.listFiles()) {
                children.add(new CifsSmbFile(file));
            }
        } catch (final SmbException e) {
            // TODO Auto-generated catch block
            Log.e(LOG_TAG, "Error listing " + m_smb_file, e);
        }
        return children;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new SmbFileInputStream(m_smb_file);
        } catch (final SmbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getSize() {
        try {
            return m_smb_file.length();
        } catch (final SmbException e) {
            Log.e(LOG_TAG, "Error getting size of " + m_smb_file, e);
            return 0;
        }
    }

    @Override
    public String toString() {
        return m_smb_file.toString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(m_smb_file.getPath());
    }

    public static final Parcelable.Creator<CifsSmbFile> CREATOR = new Creator<CifsSmbFile>() {

        @Override
        public CifsSmbFile createFromParcel(final Parcel source) {
            try {
                return new CifsSmbFile(new SmbFile(source.readString()));
            } catch (final MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public CifsSmbFile[] newArray(final int size) {
            return new CifsSmbFile[size];
        }

    };
}
