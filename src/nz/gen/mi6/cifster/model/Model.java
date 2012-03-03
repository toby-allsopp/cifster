package nz.gen.mi6.cifster.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final List<CifsDir> m_parentDirs;
    private CifsDir m_currentDir;

    public Model() {
        m_parentDirs = new ArrayList<CifsDir>();
        m_currentDir = new CifsRoot();
    }

    public List<CifsDir> getParentDirs() {
        return m_parentDirs;
    }

    public CifsDir getCurrentDir() {
        return m_currentDir;
    }

    public void setCurrentDir(CifsDir currentDir) {
        m_parentDirs.add(m_currentDir);
        m_currentDir = currentDir;
    }

}
