package nz.gen.mi6.cifster.model;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class CifsRoot implements CifsDir {

	public String getName() {
		return "/";
	}

	public List<CifsDir> getChildren() {
		List<CifsDir> children = new ArrayList<CifsDir>();
		try {
			SmbFile smb_file = new SmbFile("smb://");
			for (SmbFile file : smb_file.listFiles()) {
				children.add(new CifsWorkgroup(file));
			}
			return children;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return children;
	}

}
