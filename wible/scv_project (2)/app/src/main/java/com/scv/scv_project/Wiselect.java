package com.scv.scv_project;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;

public class Wiselect extends Activity {

    Value[] var1;
    Integer num1;
    Value[] var2;
    Integer num2;
    Value[] var3;
    Integer num3;

    private ImageView WF1_on;       // [v0.3] 가습기 on
    private ImageView WF1_off;      // [v0.3] 가습기 off
    private ImageView WF2_on;       // [v0.3] 에어컨 on
    private ImageView WF2_off;      // [v0.3] 에어컨 off
    private ImageView WF3_on;       // [v0.3] 공기청정기 on
    private ImageView WF3_off;      // [v0.3] 공기청정기 off

    private final String API_KEY = "A1E-eff520208aefbc57a8d1bee6a7d468c851a9"; // API KEY 값
    private final String VARIABLE_ID1 = "5ac4ae43c03f9709f849ce5d"; // value ID1
    private final String VARIABLE_ID2 = "5a97d554c03f970fb9698c5e"; // value ID2
    private final String VARIABLE_ID3 = "5a97f716c03f97386e5fbddd"; // value ID3

    Thread t1;
    Thread t2;
    Thread t3;

    public class ApiUbidots1 extends AsyncTask<Integer, Void, Void> { //값을 전송하는 과정을 AsyncTask 를 통해 수행.
        @Override
        protected Void doInBackground(Integer... params) { // params 인자에 0 또는 1 이 담긴다.
            ApiClient apiClient = new ApiClient(API_KEY);
            Variable batteryLevel = apiClient.getVariable(VARIABLE_ID2);

            batteryLevel.saveValue(params[0]);
            return null;
        }
    }

    public class ApiUbidots2 extends AsyncTask<Integer, Void, Void> { //값을 전송하는 과정을 AsyncTask 를 통해 수행.
        @Override
        protected Void doInBackground(Integer... params) { // params 인자에 0 또는 1 이 담긴다.
            ApiClient apiClient = new ApiClient(API_KEY);
            Variable batteryLevel = apiClient.getVariable(VARIABLE_ID3);

            batteryLevel.saveValue(params[0]);
            return null;
        }
    }

    public class ApiUbidots3 extends AsyncTask<Integer, Void, Void> { //값을 전송하는 과정을 AsyncTask 를 통해 수행.
        @Override
        protected Void doInBackground(Integer... params) { // params 인자에 0 또는 1 이 담긴다.
            ApiClient apiClient = new ApiClient(API_KEY);
            Variable batteryLevel = apiClient.getVariable(VARIABLE_ID1);

            batteryLevel.saveValue(params[0]);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);      // [v0.3] 팝업 타이틀 바 없애기
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);     // [v0.3] 배경투명처리

        setContentView(R.layout.activity_wiselect);

        android.view.WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();    // [v0.3] 팝업 위치
        layoutParams.gravity = Gravity.BOTTOM;       // [v0.3] 팝업 위치


        //--- [v0.3] ---
        WF1_on = (ImageView) findViewById(R.id.wf1_on);
        WF1_off = (ImageView) findViewById(R.id.wf1_off);
        WF2_on = (ImageView) findViewById(R.id.wf2_on);
        WF2_off = (ImageView) findViewById(R.id.wf2_off);
        WF3_on = (ImageView) findViewById(R.id.wf3_on);
        WF3_off = (ImageView) findViewById(R.id.wf3_off);
        //--------------

        t1 = new Thread() {
            public void run() { // Ubidots 에서 값을 받아오는 과정을 스레드를 통해 반복 수행한다.
                while (true) {
                    ApiClient apiClient = new ApiClient(API_KEY);
                    Variable batteryLevel = apiClient.getVariable(VARIABLE_ID2);
                    var1 = batteryLevel.getValues();
                    num1 = (int) var1[0].getValue(); // Variable[] 형에 값을 받아온 후,
                    //  첫번째 인덱스의 값을 저장한다.

                    if(num1 == 0)
                    {
                        WF1_on.post(new Runnable() {
                            @Override
                            public void run() {
                                WF1_on.setVisibility(View.GONE);
                            }
                        });
                        WF1_off.post(new Runnable() {
                            @Override
                            public void run() {
                                WF1_off.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    }
                    else
                    {
                        WF1_on.post(new Runnable() {
                            @Override
                            public void run() {
                                WF1_on.setVisibility(View.VISIBLE);
                            }
                        });
                        WF1_off.post(new Runnable() {
                            @Override
                            public void run() {
                                WF1_off.setVisibility(View.GONE);
                            }
                        });
                        break;
                    }
                }
            }
        };
        t1.start();

        t2 = new Thread() {
            public void run() { // Ubidots 에서 값을 받아오는 과정을 스레드를 통해 반복 수행한다.
                while (true) {
                    ApiClient apiClient = new ApiClient(API_KEY);
                    Variable batteryLevel = apiClient.getVariable(VARIABLE_ID3);
                    var2 = batteryLevel.getValues();
                    num2 = (int) var2[0].getValue(); // Variable[] 형에 값을 받아온 후,
                    //  첫번째 인덱스의 값을 저장한다.

                    if(num2 == 0)
                    {
                        WF2_on.post(new Runnable() {
                            @Override
                            public void run() {
                                WF2_on.setVisibility(View.GONE);
                            }
                        });
                        WF2_off.post(new Runnable() {
                            @Override
                            public void run() {
                                WF2_off.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    }
                    else
                    {
                        WF2_on.post(new Runnable() {
                            @Override
                            public void run() {
                                WF2_on.setVisibility(View.VISIBLE);
                            }
                        });
                        WF2_off.post(new Runnable() {
                            @Override
                            public void run() {
                                WF2_off.setVisibility(View.GONE);
                            }
                        });
                        break;
                    }
                }
            }
        };
        t2.start();

        t3 = new Thread() {
            public void run() { // Ubidots 에서 값을 받아오는 과정을 스레드를 통해 반복 수행한다.
                while (true) {
                    ApiClient apiClient = new ApiClient(API_KEY);
                    Variable batteryLevel = apiClient.getVariable(VARIABLE_ID1);
                    var3 = batteryLevel.getValues();
                    num3 = (int) var3[0].getValue(); // Variable[] 형에 값을 받아온 후,
                    //  첫번째 인덱스의 값을 저장한다.

                    if(num3 == 0)
                    {
                        WF3_on.post(new Runnable() {
                            @Override
                            public void run() {
                                WF3_on.setVisibility(View.GONE);
                            }
                        });
                        WF3_off.post(new Runnable() {
                            @Override
                            public void run() {
                                WF3_off.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    }
                    else
                    {
                        WF3_on.post(new Runnable() {
                            @Override
                            public void run() {
                                WF3_on.setVisibility(View.VISIBLE);
                            }
                        });
                        WF3_off.post(new Runnable() {
                            @Override
                            public void run() {
                                WF3_off.setVisibility(View.GONE);
                            }
                        });
                        break;
                    }
                }
            }
        };
        t3.start();

    }
    public void onStop(){

        super.onStop();


    }


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.wf1_on:
            case R.id.wf1_off: {
                t1.interrupt();
                if (WF1_off.getVisibility() == View.VISIBLE) {
                    new ApiUbidots1().execute(1);   // 클라우드에 1 값을 보낸다.
                    WF1_on.setVisibility(View.VISIBLE);
                    WF1_off.setVisibility(View.GONE);
                } else {
                    new ApiUbidots1().execute(0);   // 클라우드에 0 값을 보낸다.
                    WF1_on.setVisibility(View.GONE);
                    WF1_off.setVisibility(View.VISIBLE);
                }
                break;
            }
            case R.id.wf2_on:
            case R.id.wf2_off: {
                t2.interrupt();
                if (WF2_off.getVisibility() == View.VISIBLE) {
                    new ApiUbidots2().execute(1);   // 클라우드에 1 값을 보낸다.
                    WF2_on.setVisibility(View.VISIBLE);
                    WF2_off.setVisibility(View.GONE);
                } else {
                    new ApiUbidots2().execute(0);   // 클라우드에 0 값을 보낸다.
                    WF2_on.setVisibility(View.GONE);
                    WF2_off.setVisibility(View.VISIBLE);
                }
                break;
            }
            case R.id.wf3_on:
            case R.id.wf3_off: {
                t3.interrupt();
                if (WF3_off.getVisibility() == View.VISIBLE) {
                    new ApiUbidots3().execute(1);   // 클라우드에 1 값을 보낸다.
                    WF3_on.setVisibility(View.VISIBLE);
                    WF3_off.setVisibility(View.GONE);
                } else {
                    new ApiUbidots3().execute(0);   // 클라우드에 0 값을 보낸다.
                    WF3_on.setVisibility(View.GONE);
                    WF3_off.setVisibility(View.VISIBLE);
                }
                break;
            }

        }

    }
}