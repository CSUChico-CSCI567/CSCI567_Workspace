package examples.csci567.lecture5;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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
        private View rootView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Button button = (Button) rootView.findViewById(R.id.button);
            button.setOnClickListener(this);

            return rootView;
        }

        /**
         *
         * @param src is View that was clicked
         */
        public void onClick(View src){
            switch (src.getId()) {
                case R.id.button:
                    TextView tb1 = (TextView) rootView.findViewById(R.id.tb1);
                    TextView tb2 = (TextView) rootView.findViewById(R.id.tb2);
                    tb1.setText("");
                    tb2.setText("");
                    CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.cb1);
                    CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.cb2);
                    EditText ed = (EditText) rootView.findViewById(R.id.etb1);
                    if(cb1.isChecked()){
                        tb1.setText(ed.getText());
                    }
                    if(cb2.isChecked()){
                        tb2.setText(ed.getText());
                    }
                    Toast.makeText(context, "TextViews Updated", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


}
