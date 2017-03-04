package pl.edu.wat.sms_sender2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.UnsupportedSchemeException;

/**
 * Created by olszewskmate2 on 02.03.2017.
 */

public class MMSBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE="pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        throw new UnsupportedOperationException("Coming soon!");
    }
}
