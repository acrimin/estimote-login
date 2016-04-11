package com.jmarque.cpsc399project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginClick(View view) {
//        go to the logged in screen
        startActivity(new Intent(this,SignedInActivity.class));
    }

    public void registerClick(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }
}
