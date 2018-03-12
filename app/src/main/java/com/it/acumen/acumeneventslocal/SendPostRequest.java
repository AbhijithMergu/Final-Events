package com.it.acumen.acumeneventslocal;

/**
 * Created by pavan on 3/9/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class SendPostRequest extends AsyncTask<JSONObject, Void, String> {
    public AsyncResponse delegate = null;
    private Activity activity;
    private ProgressDialog pDialog;
    public SendPostRequest(){

    }
    public SendPostRequest(Activity activity){
        this.activity = activity;
        // pDialog = new ProgressDialog(activity);
    }
    protected void onPreExecute(){
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public String doInBackground(JSONObject... arg0) {

        try {


            URL url = new URL(arg0[0].getString("url"));
            JSONObject postDataParams = arg0[1];
//            URL url = new URL("http://www.acumenit.in/andy/register/fetch"); // here is your URL path

//            postDataParams.put("ideaid", 20);
//            postDataParams.put("subject", "From Android");
//            postDataParams.put("body","Check object sent");
//            postDataParams.put("date_time", DateFormat.getDateInstance().toString());
//
//            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            Log.e("Sent data : ",getPostDataString(postDataParams));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(getApplicationContext(), result,
        //    Toast.LENGTH_LONG).show();
//        JSONObject obj;
//        try {
//            obj = new JSONObject(result);
//        }
//        catch (final JSONException e)
//        {
//            obj = null;
//        }

        if (pDialog.isShowing())
            pDialog.dismiss();
        Log.e("Received Data : ",result);
        delegate.processFinish(result);
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}