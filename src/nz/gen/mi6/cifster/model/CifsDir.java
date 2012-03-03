package nz.gen.mi6.cifster.model;

import java.util.List;

public interface CifsDir {

	String getName();

	List<CifsDir> getChildren();
}
