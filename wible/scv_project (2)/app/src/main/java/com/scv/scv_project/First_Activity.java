package com.scv.scv_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class First_Activity extends Activity {

    private Handler mHandler;
    private  Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Second_Activity.class);
                startActivity(intent);
                finish();
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1500);
    }

    @Override
    protected void onDestroy(){
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
