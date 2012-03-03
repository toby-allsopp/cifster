package nz.gen.mi6.cifster.model;

import java.util.List;

public interface CifsItem {

    String getName();

    List<CifsItem> getChildren();
}
