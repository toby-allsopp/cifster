package nz.gen.mi6.cifster.model;

import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

abstract class CifsSmbFile implements CifsDir {

    private final SmbFile m_smb_file;

    public CifsSmbFile(SmbFile smb_file) {
        m_smb_file = smb_file;
    }

    @Override
    public String getName() {
        return m_smb_file.getName();
    }

    @Override
    public List<CifsDir> getChildren() {
        List<CifsDir> children = new ArrayList<CifsDir>();
        try {
            for (SmbFile file : m_smb_file.listFiles()) {
                children.add(createChild(file));
            }
        } catch (SmbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return children;
    }

    protected abstract CifsDir createChild(SmbFile smb_file);
}
