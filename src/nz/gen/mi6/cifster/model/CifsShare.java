package nz.gen.mi6.cifster.model;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class CifsShare extends CifsSmbFile implements CifsItem {

    public CifsShare(final SmbFile smb_file) {
        super(smb_file);
    }

    @Override
    protected CifsItem createChild(final SmbFile smb_file) {
        try {
            if (smb_file.isDirectory())
                return new CifsDirectory(smb_file);
            else
                return new CifsFile(smb_file);
        } catch (final SmbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
