package com.jmarque.cpsc399project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SearchingForBeaconActivity extends AppCompatActivity {

    private String URL_GET_CLASS = "https://people.cs.clemson.edu/~jmarque/cs399/php/getClass.php";
    private boolean success = false;
    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = 10.0f * 360.0f;// 3.141592654f * 32.0f;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_for_beacon);

        ImageView favicon = (ImageView) findViewById(R.id.favicon);

        RotateAnimation r; // = new RotateAnimation(ROTATE_FROM, ROTATE_TO);
//        r = new RotateAnimation(ROTATE_FROM, ROTATE_TO, 0, 0, 40, 0);

        r = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration(15000);


        r.setRepeatCount(15);
        favicon.startAnimation(r);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //startActivity(new Intent(SearchingForBeaconActivity.this, ClassOptions.class));
                //    ideally we would wait until we found the beacon. but i don't think we have time for that let's just hard code this for now ssshhhhhh
                //we need to get the class info
                new createUser().execute();
            }
        }, 1000);

    }


    private class createUser extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            URL urlObj;
            try {
                urlObj = new URL(URL_GET_CLASS);
                try {
                    HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

                    //add request header
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setAllowUserInteraction(false);
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);

                    //todo: actually find a beacon
                    String uuid = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
                    int major = 35445;
                    int minor = 37670;
                    String urlParams = "UUID=" + uuid + "&major=" + Integer.toString(major) + "&minor=" + Integer.toString(minor);

                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(urlParams);
                    wr.flush();
                    wr.close();

                    Log.e("params: ", urlParams);

                    conn.connect();

                    int serverResponseCode = conn.getResponseCode();
                    Log.i("response code: ", Integer.toString(serverResponseCode));
                    //Good return
                    if (serverResponseCode == 200 || serverResponseCode == 201) {
                        Log.i("response code2: ", Integer.toString(serverResponseCode));
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        String ret = sb.toString();
                        ret = ret.trim();

                        Log.i("return value: ", ret);

                        JSONObject jObject = new JSONObject(ret);
                        if (jObject.getInt("success") == 1) {
                            success = true;
                            int idCourse = jObject.getInt("idCourse");
                            String className = jObject.getString("className");

                            SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");


                            String startString = jObject.getString("startTime");
                            String endString = jObject.getString("endTime");
                            Date startTime = parser.parse(startString);
                            Date endTime = parser.parse(endString);

                            String profName = jObject.getString("name");


                            // save the student id to shared preferences so you can use it later.
                            SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putInt("idCourse", idCourse).apply();
                            sharedPreferences.edit().putString("className", className).apply();
                            sharedPreferences.edit().putString("startTime", startTime.toString()).apply();
                            sharedPreferences.edit().putString("endTime", endTime.toString()).apply();
                            sharedPreferences.edit().putString("profName", profName).apply();


                        }

                        return ret;
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... result) {
            super.onProgressUpdate(result);

        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (success) {
                startActivity(new Intent(SearchingForBeaconActivity.this, ClassOptions.class));
            } else {
                //no beeacon found :(
            }


        }

    }

}
