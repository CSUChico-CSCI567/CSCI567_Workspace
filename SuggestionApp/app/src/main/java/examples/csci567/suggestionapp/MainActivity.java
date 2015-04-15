package examples.csci567.suggestionapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;


public class MainActivity extends ActionBarActivity {

    private static ShareActionProvider mShareActionProvider;

    private static ActionMode mActionMode;
    private static ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_action, menu);
            try {
                /** Getting the actionprovider associated with the menu item whose id is share */
                mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.menu_item_share).getActionProvider();

                /** Setting a share intent */
                mShareActionProvider.setShareIntent(getStringShareIntent(""));
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId()== R.id.menu_item_share) {
                // Fetch and store ShareActionProvider
                //shareCurrentItem();
                mode.finish(); // Action picked, so close the CAB
                return true;
            }
            return false;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    /** Returns a share intent */
    private static Intent getStringShareIntent(String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //MIME type
        intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        return intent;
    }

    // Call to update the share intent
    private static void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener{
        View rootView;
        private ArrayAdapter<String> adapter;
        private ListView listView1;
        String [] items = {"No Suggestions"};

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            listView1 = (ListView) rootView.findViewById(R.id.listView1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
            listView1.setAdapter(adapter);
            listView1.setClickable(true);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, final int position, long arg3) {
                    Toast.makeText(getActivity(),listView1.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                }
            });

            listView1.setLongClickable(true);
            listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View view, final int position, long arg3) {
                    if (mActionMode != null) {
                        return false;
                    }

                    // Start the CAB using the ActionMode.Callback defined above
                    mActionMode = getActivity().startActionMode(mActionModeCallback);
                    setShareIntent(getStringShareIntent(listView1.getItemAtPosition(position).toString()));
                    //Toast.makeText(getBaseContext(), listView2.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                    //listView1.setItemChecked(position, true);
                    return true;

                }
            });


            new getData().execute();
            return rootView;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }


        @Override
        public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            if(item.getItemId()==R.id.edit){
                Toast.makeText(getActivity(), "Edit: " + adapter.getItem(position), Toast.LENGTH_LONG).show();
                return true;
            }
            if(item.getItemId()==R.id.delete){
                Toast.makeText(getActivity(), "Delete: "+ adapter.getItem(position), Toast.LENGTH_LONG).show();
                return true;
            }
            return super.onContextItemSelected(item);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
        private class getData extends AsyncTask<Void, Void, Void> {
            InputStream inputStream = null;
            String result = "";
            String url_select = "http://www.bryancdixon.com/androidjson";
            Vector<String> results = new Vector<String>();


            protected void onPreExecute() {
                Log.d("SuggestionAPP ", "Preparing to get Suggestions");
            }

            protected Void doInBackground(Void... params) {
                try {
                    URI uri = new URI(url_select);
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpclient.execute(new HttpGet(uri));
                    HttpEntity httpEntity = httpResponse.getEntity();
                    inputStream = httpEntity.getContent();
                } catch (UnsupportedEncodingException e1) {
                    Log.e("UnsupportedEncoding", e1.toString());
                    e1.printStackTrace();
                } catch (ClientProtocolException e2) {
                    Log.e("ClientProtocolException", e2.toString());
                    e2.printStackTrace();
                } catch (IllegalStateException e3) {
                    Log.e("IllegalStateException", e3.toString());
                    e3.printStackTrace();
                } catch (IOException e4) {
                    Log.e("IOException", e4.toString());
                    e4.printStackTrace();
                } catch (URISyntaxException e) {
                    Log.e("URISyntaxException ", e.toString());
                    e.printStackTrace();
                }
                // Convert response to string using String Builder
                try {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                    StringBuilder sBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }
                    inputStream.close();
                    result = sBuilder.toString();

                } catch (Exception e) {
                    Log.e("String & BufferedReader", "Error converting result " + e.toString());
                }
                return null;
            }

            protected void onPostExecute(Void donothing) {
                //parse JSON data
                String text = "";
                try {
                    JSONObject jO = new JSONObject(result);
                    JSONArray jArray = jO.getJSONArray("suggestions");


                    for(int i=0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        results.add(jObject.getString("text"));
                        text += jObject.getString("text")+"\n\n";
                        //Log.d("SuggestionAPP ",text);

                    } // End Loop
                    if(jArray.length()<=0){
                        results.add("No Suggestions");
                        text="No Suggestions";
                    }
                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                    results.add("No Suggestions");
                    text="No Suggestions";
                }
                //Generate String Array from Vector
                String [] s =  results.toArray(new String[results.size()]);


                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, s);

                listView1.setAdapter(adapter);
                //Method when using textview
                //txt.setText(text);

                //set TextView Contents to be JSON response
                //txt.setText(result);
            }
        }
    }
}
