package com.jmarque.cpsc399project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AnswerActivity extends AppCompatActivity {

    private String URL_ANSWER_QUESTION = "https://people.cs.clemson.edu/~jmarque/cs399/php/answerQuestion.php";
    private int idCourse;
    private int idStudent;
    private Button button;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        idCourse = getIntent().getIntExtra("idCourse", -1);
        idStudent = getIntent().getIntExtra("idStudent", -1);

    }

    public void btnClick(View view) {
        button = (Button) view;
        answer = button.getText().toString();
        new answerQuestion().execute();
    }


    private class answerQuestion extends AsyncTask<String, String, String> {
        boolean success = false;

        protected String doInBackground(String... params) {
            URL urlObj;
            try {
                urlObj = new URL(URL_ANSWER_QUESTION);
                try {
                    HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

                    //add request header
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setAllowUserInteraction(false);
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);

//                    Log.i("params: ", params[0] + "," + params[1]);
                    String urlParams = "idCourse=" + idCourse;
                    urlParams += "&idStudent=" + idStudent;
                    urlParams += "&answer=" + answer;

                    Log.i("Sending", urlParams);

                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(urlParams);
                    wr.flush();
                    wr.close();


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
                            Log.e("Success", "it was a success");

                        }

                        return ret;
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (success) {
            }


        }

    }
}
