package nz.gen.mi6.cifster.model;

import java.io.InputStream;
import java.util.List;

import android.os.Parcelable;

public interface CifsItem extends Parcelable {

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

    InputStream getInputStream();
}
