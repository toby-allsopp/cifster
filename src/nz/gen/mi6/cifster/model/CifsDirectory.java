package nz.gen.mi6.cifster.model;

import jcifs.smb.SmbFile;

public class CifsDirectory extends CifsSmbFile {

	public CifsDirectory(SmbFile smb_file) {
		super(smb_file);
	}

	@Override
	protected CifsDir createChild(SmbFile smb_file) {
		return new CifsFile(smb_file);
	}

}
