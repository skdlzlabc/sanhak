package com.scv.scv_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;


public class Second_Activity extends Activity {

    private final String API_KEY = "A1E-eff520208aefbc57a8d1bee6a7d468c851a9"; // API KEY 값
    Value[] var1;
    Integer num1;
    Value[] var2;
    Integer num2;
    Value[] var3;
    Integer num3;

    TextView text1;
    TextView text2;
    TextView text3;

    ImageView btn1;     // [v0.9] 세팅 버튼 추가
    Integer noti;       // [v0.9] 노티값 (0:알림끄기, 1:알림키기)

    private long backKeyPressedTime = 0;    // [v0.6] 뒤로 가기 버튼 두번 눌러 종료
    private Toast toast;                    // [v0.6]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent si = new Intent(this, NotiService.class);
        stopService(si);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);

        btn1 = (ImageView)findViewById(R.id.setting);      // [v0.9] 세팅 버튼 추가
        noti = 1;   // [v0.9] 노티값 (0:알림끄기, 1:알림키기)

        Thread t1 = new Thread(){
            public void run(){ // Ubidots 에서 값을 받아오는 과정을 스레드를 통해 반복 수행한다.
                while(true){
                    ApiClient apiClient = new ApiClient(API_KEY);
                    Variable batteryLevel = apiClient.getVariable("5a5c88b0c03f9779be224d28");
                    var1=batteryLevel.getValues();
                    num1=(int)var1[0].getValue(); // Variable[] 형에 값을 받아온 후,
                    //  첫번째 인덱스의 값을 저장한다.

                    if(num1 < 30)
                    {
                        text1.post(new Runnable() {
                            @Override
                            public void run() {
                                text1.setTextColor(Color.parseColor("#23D600"));
                                text1.setText("좋음"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }
                    else if(num1 >= 30 && num1 < 80)
                    {
                        text1.post(new Runnable() {
                            @Override
                            public void run() {
                                text1.setTextColor(Color.parseColor("#DBDB00"));
                                text1.setText("보통"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }
                    else if(num1 >= 80 && num1 < 150)
                    {
                        text1.post(new Runnable() {
                            @Override
                            public void run() {
                                text1.setTextColor(Color.parseColor("#D80000"));
                                text1.setText("나쁨"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }
                    else if(num1 >= 150)
                    {
                        text1.post(new Runnable() {
                            @Override
                            public void run() {
                                text1.setTextColor(Color.parseColor("#D600C7"));
                                text1.setText("비상"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }

                }
            }
        };
        t1.start();

        Thread t2 = new Thread(){
            public void run(){ // Ubidots 에서 값을 받아오는 과정을 스레드를 통해 반복 수행한다.
                while(true){
                    ApiClient apiClient = new ApiClient(API_KEY);
                    Variable batteryLevel = apiClient.getVariable("5a97d545c03f971038188d8e");
                    var2=batteryLevel.getValues();
                    num2=(int)var2[0].getValue(); // Variable[] 형에 값을 받아온 후,
                    //  첫번째 인덱스의 값을 저장한다.

                    if(num2 < 10 && num2 >= 0)
                    {
                        text2.post(new Runnable() {
                            @Override
                            public void run() {
                                text2.setText("  " + num2.toString() + "%"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }
                    else
                    {
                        text2.post(new Runnable() {
                            @Override
                            public void run() {
                                text2.setText(num2.toString() + "%"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }

                }
            }
        };
        t2.start();

        Thread t3 = new Thread(){
            public void run(){ // Ubidots 에서 값을 받아오는 과정을 스레드를 통해 반복 수행한다.
                while(true){
                    ApiClient apiClient = new ApiClient(API_KEY);
                    Variable batteryLevel = apiClient.getVariable("5a97f71dc03f97388b2635e1");
                    var3=batteryLevel.getValues();
                    num3=(int)var3[0].getValue(); // Variable[] 형에 값을 받아온 후,
                    //  첫번째 인덱스의 값을 저장한다.

                    if(num3 < 10 && num3 >= 0)
                    {
                        text3.post(new Runnable() {
                            @Override
                            public void run() {
                                text3.setText("  " + num3.toString() + "℃"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }
                    else
                    {
                        text3.post(new Runnable() {
                            @Override
                            public void run() {
                                text3.setText(num3.toString() + "℃"); // TextView 의 값을 갱신한다.

                            }
                        });
                    }
                }
            }
        };
        t3.start();


    }

    public void myClick(View v)
    {
        switch (v.getId())
        {

            case R.id.Wifi_Background:
            {
                Intent intent = new Intent(this, Wiselect.class);
                startActivity(intent);
                break;
            }

            case R.id.Wifi:
            {
                Intent intent = new Intent(this, Wiselect.class);
                startActivity(intent);
                break;
            }

            case R.id.Bluetooth_Background:
            {
                Intent intent = new Intent(this, Bluetooth_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            }
            case R.id.Bluetooth:
            {
                Intent intent = new Intent(this, Bluetooth_Activity.class);
                startActivity(intent);
                break;
            }

            // [v0.9] 노티 ON/OFF
            case R.id.setting: {
                if(noti == 0) {
                    toast = Toast.makeText(this, "알림 ON", Toast.LENGTH_SHORT);
                    toast.show();
                    noti = 1;
                }
                else {
                    toast = Toast.makeText(this, "알림 OFF", Toast.LENGTH_SHORT);
                    toast.show();
                    noti = 0;
                }
                break;
            }

        }
    }


    @Override
    public void onBackPressed() {
        // [v0.6] 뒤로가기 버튼 눌렀을 때, 종료
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // [v0.9] noti=1 일 때, 서비스 돌림.
            if(noti == 1){
                Intent si = new Intent(this, NotiService.class);
                startService(si);
            }
            ActivityCompat.finishAffinity(this);
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
    }
    public void showGuide() {
        toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
