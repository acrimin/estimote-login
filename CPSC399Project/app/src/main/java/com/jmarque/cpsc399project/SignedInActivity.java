package com.jmarque.cpsc399project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SignedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        TextView username = (TextView) findViewById(R.id.mainActivity_username);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        username.setText(sharedPreferences.getString("username","null"));




    }

    public void signInClick(View view) {
        startActivity(new Intent(this, SearchingForBeaconActivity.class));
        // we don't really do anything here so let's just go to the next screen
    }
}
