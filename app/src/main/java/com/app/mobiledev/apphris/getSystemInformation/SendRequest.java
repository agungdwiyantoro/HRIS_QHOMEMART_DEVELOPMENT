package com.app.mobiledev.apphris.getSystemInformation;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.app.mobiledev.apphris.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class SendRequest extends AsyncTask<String, Void, String> {


    protected void onPreExecute(){}

    protected String doInBackground(String... arg0) {

        try{

            URL url = new URL("https://script.google.com/macros/s/AKfycbzE5YlPCWHC_VG23NQpgmpjCsySXG2fS9WsmZ66N45pUhq2Ion-eYQhpPCPTlMItSi_SA/exec");
            // https://script.google.com/macros/s/AKfycbyuAu6jWNYMiWt9X5yp63-hypxQPlg5JS8NimN6GEGmdKZcIFh0/exec
            JSONObject postDataParams = new JSONObject();

            //int i;
            //for(i=1;i<=70;i++)



            //    String usn = Integer.toString(i);

            //String id= "AKfycbx4jW3PmNfZUbGgPHNosTBGNgpXfyZEDoOohBnhp5k9Jcfvg0HC54I2NlklhDMQyXAxEA";

            //postDataParams.put("serial", Build.SERIAL);
            postDataParams.put("model",Build.MODEL);
            postDataParams.put("id", Build.ID);
            postDataParams.put("manufacturer",Build.MANUFACTURER);
            postDataParams.put("brand",Build.BRAND);
            //postDataParams.put("type",Build.TYPE);
            //postDataParams.put("user", Build.USER);
            //postDataParams.put("base",Build.VERSION_CODES.BASE);
            //postDataParams.put("incremental", Build.VERSION.INCREMENTAL);
            postDataParams.put("sdk", Build.VERSION.SDK_INT);
            //postDataParams.put("board", Build.BOARD);
            //postDataParams.put("host", Build.HOST);
            //postDataParams.put("fingerprint", Build.FINGERPRINT);
            postDataParams.put("versioncode", Build.VERSION.RELEASE);


            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
               // Toast.LENGTH_LONG).show();

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
