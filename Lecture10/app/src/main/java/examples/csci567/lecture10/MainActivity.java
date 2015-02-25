package examples.csci567.lecture10;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        try {

        }
        catch (Exception e){
            Log.e("Lecture10: ", e.toString());
        }
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
    public static class PlaceholderFragment extends Fragment implements OnClickListener{
        View rootView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Button button1 = (Button) rootView.findViewById(R.id.button);
            Button button2 = (Button) rootView.findViewById(R.id.button2);
            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            return rootView;
        }
        @Override
        public void onClick(View src) {
            switch (src.getId()) {
                case R.id.button: //Write Button
                    EditText et = (EditText) rootView.findViewById(R.id.editbox);
                    String text = et.getText().toString();
                    try {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.txt");
                        FileWriter filewriter = new FileWriter(file);
                        BufferedWriter wbuf = new BufferedWriter(filewriter);
                        wbuf.write(text);
                        wbuf.close();
                    }
                    catch(Exception e){
                        Log.e("Lecture 10: ", e.toString());
                    }

                    break;
                case R.id.button2: //Read Button
                    String contents = "";
                    try {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.txt");
                        FileReader filereader = new FileReader(file);
                        BufferedReader rbuf = new BufferedReader(filereader);
                        contents = rbuf.readLine();
                    }
                    catch(Exception e){
                        Log.e("Lecture 10: ", e.toString());
                        contents="";
                    }
                    finally {
                        TextView tv = (TextView) rootView.findViewById(R.id.textbox);
                        tv.setText(contents);
                    }
                    break;
            }
        }
    }
}
