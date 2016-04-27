package com.jmarque.cpsc399project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClassOptions extends AppCompatActivity {
    private int idCourse;
    private int idStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_options);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        idCourse = sharedPreferences.getInt("idCourse", -1);
        idStudent = sharedPreferences.getInt("idStudent", -1);
        String username = sharedPreferences.getString("username", "null");
        String className = sharedPreferences.getString("className", "null");
        String startTime = sharedPreferences.getString("startTime", "0:00");
        String endTime = sharedPreferences.getString("endTime", "0:00");
        String profName = sharedPreferences.getString("profName", "null");

        String header = "Class: " + className + "\n";
        header += "Instructor: " + profName + "\n";
        header += "Time: " + startTime + " - " + endTime + "\n";
        header += "Username: " + username + "\n";

        TextView textView = (TextView) findViewById(R.id.textViewInfo);
        textView.setText(header);
    }

    public void askClick(View view) {
        Intent intent = new Intent(this, AskActivity.class);
        intent.putExtra("idCourse", idCourse);
        intent.putExtra("idStudent", idStudent);
        startActivity(intent);
    }

    public void answerClick(View view) {
        Intent intent = new Intent(this, AnswerActivity.class);
        intent.putExtra("idCourse", idCourse);
        intent.putExtra("idStudent", idStudent);
        startActivity(intent);
    }
}
