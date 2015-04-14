package examples.csci567.suggestionapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
            new getData().execute();
            return rootView;
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
                        Log.d("SuggestionAPP ",text);

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
