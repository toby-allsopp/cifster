package nz.gen.mi6.cifster.operation;

import android.app.Notification;
import android.content.Context;
import android.os.Parcelable;
import android.util.Pair;

public interface NotifiableOperation extends Parcelable {

    Pair<Integer, Notification> createNotification(Context context);

    void run(Context context);

}
