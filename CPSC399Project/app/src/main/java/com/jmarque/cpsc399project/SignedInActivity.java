package com.jmarque.cpsc399project;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        SharedPreferences sharedPref = this.getSharedPreferences("com.jmarque.cpsc399project", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "username");

        TextView textView = (TextView) findViewById(R.id.signedIn_username);
        textView.setText(username);
    }

    public void signInClick(View view) {
        startActivity(new Intent(this, SearchingForBeaconActivity.class));
    }
}
