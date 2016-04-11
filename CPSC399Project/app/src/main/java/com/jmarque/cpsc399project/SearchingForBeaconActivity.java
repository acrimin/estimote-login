package com.jmarque.cpsc399project;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SearchingForBeaconActivity extends AppCompatActivity {


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
//        while (true)
        favicon.startAnimation(r);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SearchingForBeaconActivity.this, ClassOptions.class));
            }
        }, 1000);

    }


}
