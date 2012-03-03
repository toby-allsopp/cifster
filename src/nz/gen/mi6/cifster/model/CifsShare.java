package nz.gen.mi6.cifster.model;

import jcifs.smb.SmbFile;

public class CifsShare extends CifsSmbFile implements CifsDir {

	public CifsShare(SmbFile smb_file) {
		super(smb_file);
	}

	@Override
	protected CifsDir createChild(SmbFile smb_file) {
		return new CifsDirectory(smb_file);
	}

}
