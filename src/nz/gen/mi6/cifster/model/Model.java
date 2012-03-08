package nz.gen.mi6.cifster.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final List<CifsItem> m_parentDirs;
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

    public void setCurrentDir(final CifsItem currentDir) {
        m_parentDirs.add(m_currentDir);
        m_currentDir = currentDir;
    }

}
