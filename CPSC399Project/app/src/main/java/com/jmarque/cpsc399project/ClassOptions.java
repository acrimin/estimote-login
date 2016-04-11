package com.jmarque.cpsc399project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ClassOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_options);
    }

    public void askClick(View view) {
        startActivity(new Intent(this, AskActivity.class));
    }

    public void answerClick(View view) {
        startActivity(new Intent(this, AnswerActivity.class));
    }
}
