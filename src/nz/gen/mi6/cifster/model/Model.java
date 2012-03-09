package nz.gen.mi6.cifster.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {

    private List<CifsItem> m_parentDirs;
    private CifsItem m_currentDir;

    public Model() {
        m_parentDirs = new ArrayList<CifsItem>();
        m_currentDir = new CifsSmbFile();
    }

    public List<CifsItem> getParentDirs() {
        return m_parentDirs;
    }

    public CifsItem getCurrentDir() {
        return m_currentDir;
    }

    public void enterChild(final CifsItem child) {
        m_parentDirs.add(m_currentDir);
        m_currentDir = child;
    }

    public void rewindToParent(final int index) {
        m_currentDir = m_parentDirs.get(index);
        m_parentDirs = m_parentDirs.subList(0, index);
    }

    public void rewindToParent(final CifsItem parent) {
        rewindToParent(m_parentDirs.indexOf(parent));
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        final CifsItem[] parents = new CifsItem[m_parentDirs.size()];
        dest.writeParcelableArray(m_parentDirs.toArray(parents), flags);
        dest.writeParcelable(m_currentDir, flags);
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {

        @Override
        public Model createFromParcel(final Parcel source) {
            final Model model = new Model();
            model.m_parentDirs = Arrays.asList((CifsItem[]) source.readParcelableArray(null));
            model.m_currentDir = source.readParcelable(null);
            return model;
        }

        @Override
        public Model[] newArray(final int size) {
            return new Model[size];
        }
    };
}
