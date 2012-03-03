package nz.gen.mi6.cifster.model;

import jcifs.smb.SmbFile;

public class CifsFile extends CifsSmbFile {

	public CifsFile(SmbFile smb_file) {
		super(smb_file);
	}

	@Override
	protected CifsDir createChild(SmbFile smb_file) {
		// TODO Auto-generated method stub
		return null;
	}

}
