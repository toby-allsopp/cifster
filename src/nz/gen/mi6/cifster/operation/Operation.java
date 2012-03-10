package nz.gen.mi6.cifster.operation;

import android.content.Context;
import android.os.Parcelable;

public interface Operation extends Parcelable {

    CharSequence getNotificationTitle(Context context);

    CharSequence getNotificationText(Context context);

    void run();

}
