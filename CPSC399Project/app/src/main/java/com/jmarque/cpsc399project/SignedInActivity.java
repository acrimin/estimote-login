package com.jmarque.cpsc399project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
    }

    public void signInClick(View view) {
        startActivity(new Intent(this, SearchingForBeaconActivity.class));
    }
}
