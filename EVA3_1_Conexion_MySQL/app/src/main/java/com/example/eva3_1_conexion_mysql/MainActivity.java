package com.example.eva3_1_conexion_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listActors;
    ArrayList<JSONObject> jsonList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listActors = findViewById(R.id.listActors);
        //new Connection().execute("http://5ba984f3.ngrok.io/actors");
        new ConnectionOthers().execute("http://5ba984f3.ngrok.io/actors/199");
    }

    class Connection extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String sUrl = strings[0];
            String sRes = null;

            try {
                URL url = new URL(sUrl);
                HttpURLConnection http = (HttpURLConnection)url.openConnection();

                if(http.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader inputStreamReader = new InputStreamReader(http.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    sRes = bufferedReader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sRes;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        jsonList.add(jsonObject);
                    }
                    listActors.setAdapter(new ActorAdapter(
                            MainActivity.this,
                            R.layout.layout_actor,
                            jsonList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ConnectionOthers extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String sUrl = strings[0];
            String sRes = null;

            try {
                URL url = new URL(sUrl);
                HttpURLConnection http = (HttpURLConnection)url.openConnection();

                //http.setRequestMethod("POST");
                //http.setRequestMethod("PUT");
                http.setRequestMethod("DELETE");
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("Content-type", "application/json;charset=utf-8");
                http.connect();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("first_name", "John");
                jsonObject.put("last_name", "Smith Joto");

                DataOutputStream dataOutputStream = new DataOutputStream(http.getOutputStream());
                dataOutputStream.write(jsonObject.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();

                InputStreamReader inputStreamReader = new InputStreamReader(http.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                sRes = bufferedReader.readLine();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sRes;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
