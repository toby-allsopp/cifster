package nz.gen.mi6.cifster.model;

import java.util.List;

public interface CifsItem {

    public enum Type {
        ROOT,
        WORKGROUP,
        SERVER,
        SHARE,
        DIRECTORY,
        FILE,
    }

    String getName();

    Type getType();

    List<CifsItem> getChildren();
}
