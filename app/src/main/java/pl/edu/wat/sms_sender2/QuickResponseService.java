package pl.edu.wat.sms_sender2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by olszewskmate2 on 02.03.2017.
 */

public class QuickResponseService extends Service {

    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        return super.onStartCommand(intent, flags, startID);
    }
}
