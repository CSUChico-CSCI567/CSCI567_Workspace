package examples.csci567.lecture7;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by bryandixon on 2/5/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private static final String TAG = "Lecture7-Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "ALARM");
    }

    public void setAlarm(Context context){
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);*/
        /*
        * If you don't have precise time requirements, use an inexact repeating alarm
        * the minimize the drain on the device battery.
        *
        * The call below specifies the alarm type, the trigger time, the interval at
        * which the alarm is fired, and the alarm's associated PendingIntent.
        * It uses the alarm type RTC_WAKEUP ("Real Time Clock" wake up), which wakes up
        * the device and triggers the alarm according to the time of the device's clock.
        *
        * Alternatively, you can use the alarm type ELAPSED_REALTIME_WAKEUP to trigger
        * an alarm based on how much time has elapsed since the device was booted. This
        * is the preferred choice if your alarm is based on elapsed time--for example, if
        * you simply want your alarm to fire every 60 minutes. You only need to use
        * RTC_WAKEUP if you want your alarm to fire at a particular date/time. Remember
        * that clock-based time may not translate well to other locales, and that your
        * app's behavior could be affected by the user changing the device's time setting.
        *
        * Here are some examples of ELAPSED_REALTIME_WAKEUP:
        *
        * // Wake up the device to fire a one-time alarm in one minute.
        * alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        * SystemClock.elapsedRealtime() +
        * 60*1000, alarmIntent);
        *
        * // Wake up the device to fire the alarm in 30 minutes, and every 30 minutes
        * // after that.
        * alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        * AlarmManager.INTERVAL_HALF_HOUR,
        * AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
        */
        // Set the alarm to fire at approximately 8:30 a.m., according to the device's
        // clock, and to repeat once a day.
        //alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
        // calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        //Fire Repeating Alarm every one minute.
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_FIFTEEN_MINUTES/15/4,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES/15/4, alarmIntent);
    }
    /**
     * Cancels the alarm.
     * @param context
     */
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }


}