package nz.gen.mi6.cifster.operation;

import android.app.Notification;
import android.content.Context;
import android.os.Parcelable;
import android.util.Pair;

public interface Operation extends Parcelable {

    Pair<Integer, Notification> getNotification(Context context);

    void run();

}
