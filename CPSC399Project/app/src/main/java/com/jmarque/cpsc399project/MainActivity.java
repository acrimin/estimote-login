package com.jmarque.cpsc399project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String URL_CREATE_USER = "https://people.cs.clemson.edu/~jmarque/cs399/php/registerStudent.php";
    private JSONParser jsonParser = new JSONParser();
    private JSONArray arr;
    private String username;
    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.mainActivity_username);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("username","").equals("")){
            startActivity(new Intent(this,SignedInActivity.class));
        }

    }

    public void loginClick(View view) {
//        go to the logged in screen
        username = editText.getText().toString();
        if (username.equals("")) {
            // username field is empty can't do anything with an empty field
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_LONG).show();
        } else {
            new createUser().execute();

            editText.setText(username);
        }
//        startActivity(new Intent(this,SignedInActivity.class));
    }

    public void registerClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private class createUser extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            URL urlObj;
            try {
                urlObj = new URL(URL_CREATE_USER);
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
                    String urlParams = "username=" + username;// + "&password=" + params[1];

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
                            int idStudent = jObject.getInt("id");
                            // save the student id to shared preferences so you can use it later.
                            SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("username", username).apply();
                            sharedPreferences.edit().putInt("idStudent", idStudent).apply();

                            Log.e("Success", "it was a success");

                        }
//                        Log.e("json object?",jObject.toString());
//                        JSONArray jArray = jObject.getJSONArray("myArray");


//                        publishProgress(ret);
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

        protected void onProgressUpdate(String... result) {
            super.onProgressUpdate(result);
         /*   Log.i("on progress update: ", result[0]);
            System.out.println(result[0]);
            if (result[0].equals("1")){
                Toast.makeText(MainActivity.this, "sign up successfully!", Toast.LENGTH_SHORT).show();
            }
            else if (result[0].equals("2")){
                Toast.makeText(MainActivity.this, "username already exists.", Toast.LENGTH_SHORT).show();
            }
            else if (result[0].equals("3")){
                Toast.makeText(MainActivity.this, "can't connect to database.", Toast.LENGTH_SHORT).show();
            }
            else if (result[0].equals("4")){
                Toast.makeText(MainActivity.this, "username or password is empty", Toast.LENGTH_SHORT).show();
            }
            else if (result[0].equals("5")){
                Toast.makeText(MainActivity.this, "isset wrong", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "something else happened", Toast.LENGTH_SHORT).show();
            }*/
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (success) {
                startActivity(new Intent(MainActivity.this, SignedInActivity.class));
            }


        }

    }
}
