package nz.gen.mi6.cifster.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

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
}
