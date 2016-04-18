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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (checkPreviousLogin()) {
            startActivity(new Intent(this ,SignedInActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.signedIn_username);
    }

    private boolean checkPreviousLogin() {
        SharedPreferences sharedPref = this.getSharedPreferences("com.jmarque.cpsc399project", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");
        return username != "";
    }

    public void loginClick(View view) {
//        go to the logged in screen
        username = editTextUsername.getText().toString();
        if (username.isEmpty())
            return;

        CreateUser task = new CreateUser();
        task.execute("https://people.cs.clemson.edu/~jmarque/cs399/php/registerStudent.php", username);
    }

    public void registerClick(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public class CreateUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;

            try {
                url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setAllowUserInteraction(false);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                String urlParams = "username=" + params[1];

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParams);
                wr.flush();
                wr.close();

                conn.connect();

                int serverResponseCode = conn.getResponseCode();
                Log.i("Response Code", Integer.toString(serverResponseCode));
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                String ret = sb.toString();
                ret = ret.trim();

                return ret;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Website Content", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("success") == 1) {
                    int userID = jsonObject.getInt("id");
                    SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("com.jmarque.cpsc399project", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", username);
                    editor.putInt("userID", userID);
                    editor.commit();
                    startActivity(new Intent(MainActivity.this,SignedInActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
