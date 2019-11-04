package com.example.squadup;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JSONParser {

    final static String url = "http://10.0.2.2:3000/";

    public static String formatDataAsJSON(){
        final JSONObject root = new JSONObject();
        try {
            root.put("user", "Thomas");
            root.put("password", "fakepassword");

            return root.toString();
        } catch (JSONException e) {
            Log.d("JSONParser", "Can't format JSON");
        }

        return null;
    }

    public static void sendDataToServer(){

        final String json = formatDataAsJSON();

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params){

                return getServerResponse(json);
            }

            @Override
            protected void onPostExecute(String result) {

                //Use this space to set textviews to update GUI with post-ed data
                super.onPostExecute(result);
            }

        }.execute();


    }

    private static String getServerResponse(String json) {

        HttpPost post = new HttpPost(url);

        try {
            StringEntity entity = new StringEntity(json);

            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");

            DefaultHttpClient client = new DefaultHttpClient();

            BasicResponseHandler handler = new BasicResponseHandler();

            String response = client.execute(post, handler);

            return response;

        } catch (UnsupportedEncodingException e) {
            Log.d("JSONParser", e.toString());
        } catch (ClientProtocolException e) {
            Log.d("JSONParser", e.toString());
        } catch (IOException e) {
            Log.d("JSONParser", e.toString());
        }

        return null;
    }
}
