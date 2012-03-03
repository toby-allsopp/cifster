package nz.gen.mi6.cifster.model;

import jcifs.smb.SmbFile;

public class CifsServer extends CifsSmbFile {

    public CifsServer(SmbFile smb_file) {
        super(smb_file);
    }

    @Override
    protected CifsDir createChild(SmbFile smb_file) {
        return new CifsShare(smb_file);
    }

}
