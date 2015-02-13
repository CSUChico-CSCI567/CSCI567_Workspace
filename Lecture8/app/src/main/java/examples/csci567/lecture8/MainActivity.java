package examples.csci567.lecture8;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "Lecture8-MainActivity";
    public static final String PREFS_NAME = "MyPrefsFile";
    private static Context context;
    private static int mId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        context = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener{
        private View rootView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.cb1);
            cb1.setOnClickListener(this);
            // Restore preferences
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean cb1s = settings.getBoolean("cb1", false);
            cb1.setChecked(cb1s);
            CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.cb2);
            cb2.setOnClickListener(this);
            // Restore preferences
            boolean cb2s = settings.getBoolean("cb2", false);
            cb2.setChecked(cb2s);

            Button button = (Button) rootView.findViewById(R.id.ln);
            button.setOnClickListener(this);
            return rootView;
        }

        /**
         *
         * @param src is View that was clicked
         */
        public void onClick(View src) {
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            switch (src.getId()) {
                case R.id.cb1:
                    Log.d(TAG, "Checkbox 1 clicked");
                    CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.cb1);
                    if(cb1.isChecked()){
                        editor.putBoolean("cb1", true);
                    }
                    else{
                        editor.putBoolean("cb1", false);
                    }
                    // Commit the edits!
                    editor.commit();
                    break;
                case R.id.cb2:
                    Log.d(TAG, "Checkbox 2 clicked");
                    CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.cb2);
                    if(cb2.isChecked()){
                        editor.putBoolean("cb2", true);
                    }
                    else{
                        editor.putBoolean("cb2", false);
                    }
                    // Commit the edits!
                    editor.commit();
                    break;
                case R.id.ln:
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_icon)
                                    .setContentTitle("CSCI567 notification")
                                    .setContentText("Hello World!");
                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    // Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(MainActivity.class);
                    // Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    // mId allows you to update the notification later on.
                    mId++;
                    mNotificationManager.notify(mId, mBuilder.build());

                    break;
            }
        }
    }
}
