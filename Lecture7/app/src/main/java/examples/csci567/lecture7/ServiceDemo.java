package examples.csci567.lecture7;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by bryandixon on 2/10/15.
 */
public class ServiceDemo extends Service {
    private AlarmReceiver alarm;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        alarm = new AlarmReceiver();
        alarm.setAlarm(this);
    }
}
