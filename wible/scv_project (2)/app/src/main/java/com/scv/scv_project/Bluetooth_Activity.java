
/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.scv.scv_project;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class Bluetooth_Activity extends Activity {
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final String TAG = "nRFUART";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private int mState = UART_PROFILE_DISCONNECTED;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;
    private ArrayAdapter<String> listAdapter;
    private Button btnConnectDisconnect;


    private ImageView Led_btn;         // [v0.03] LED 버튼
    private ImageView Fume_btn;        // [v0.03] 모기퇴치제 버튼
    private ImageView Fan_btn;         // [v0.03] 선풍기 버튼

    private String isOnOff = null;          // [v0.04] On/Off 상태 문자열
    private String tempData;                // [v0.04] 임시 데이터

    private ImageView BT1_on;       // [v0.3] 스탠드 on
    private ImageView BT1_off;      // [v0.3] 스탠드 off
    private ImageView BT2_on;       // [v0.3] 모기퇴치제 on
    private ImageView BT2_off;      // [v0.3] 모기퇴치제 off
    private ImageView BT3_on;       // [v0.3] 선풍기 on
    private ImageView BT3_off;      // [v0.3] 선풍기 off


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);      // [v0.3] 팝업 타이틀 바 없애기
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);     // [v0.3] 배경투명처리

        setContentView(R.layout.activity_bluetooth);

        android.view.WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();    // [v0.3] 팝업 위치
        layoutParams.gravity = Gravity.BOTTOM;       // [v0.3] 팝업 위치


        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        listAdapter = new ArrayAdapter<String>(this, R.layout.message_detail);
        btnConnectDisconnect=(Button) findViewById(R.id.btn_select);
        service_init();

        //=== 블루투스 연결 또는 연결 해제 버튼 담당 ===
        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBtAdapter.isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                else {
                    if (btnConnectDisconnect.getText().equals("장치 검색")){

                        //"장치 검색" 버튼을 누를 시, DeviceListActivity.class 파일을 열어서, 블루투스 장치를 찾는 창이 팝업 됨.
                        Intent newIntent = new Intent(Bluetooth_Activity.this, DeviceListActivity.class);
                        startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
                    }
                    else {
                        //"Disconnect" 버튼을 누를 시, 블루투스 연결 해제.
                        if (mDevice != null)
                        {
                            mService.disconnect();
                        }
                    }
                }
            }
        });


        //--- [v0.03] ---
        Led_btn = (ImageView)findViewById(R.id.bt_LedBtn);
        Fume_btn = (ImageView)findViewById(R.id.bt_FumeBtn);
        Fan_btn = (ImageView)findViewById(R.id.bt_FanBtn);
        //---------------

        //--- [v0.3] ---
        BT1_on = (ImageView)findViewById(R.id.bt1_on);
        BT1_off = (ImageView)findViewById(R.id.bt1_off);
        BT2_on = (ImageView)findViewById(R.id.bt2_on);
        BT2_off = (ImageView)findViewById(R.id.bt2_off);
        BT3_on = (ImageView)findViewById(R.id.bt3_on);
        BT3_off = (ImageView)findViewById(R.id.bt3_off);
        //--------------

        //--- [v0.6] ---
        Led_btn.setEnabled(false);
        Fume_btn.setEnabled(false);
        Fan_btn.setEnabled(false);
        //--------------
    }

    //=== Send 버튼 함수화 === [v0.01]
    public void Send(String msg){
        String message = msg;
        byte[] value;
        try {
            //send data to service
            value = message.getBytes("UTF-8");
            mService.writeRXCharacteristic(value);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //========================

    //=== UART 서비스 연결/연결 해제 ===
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mService = null;
        }
    };

    private Handler mHandler = new Handler() {
        @Override

        // UART 서비스로부터 오는 이벤트를 담당하는 핸들러
        public void handleMessage(Message msg) {

        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            final Intent mIntent = intent;

            // 기기 연결시
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_CONNECT_MSG");
                        btnConnectDisconnect.setText("연결 해제");                   // 버튼명을 "연결 해제"로 변경
                        ((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - 연결 완료");
                        showMessage("[" + mDevice.getName()+"] 기기 연결 성공");             // [v0.03]
                        mState = UART_PROFILE_CONNECTED;

                        changeState(true);      // [v0.04]

                    }
                });
            }

            // 기기 해제시
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_DISCONNECT_MSG");
                        btnConnectDisconnect.setText("장치 검색");                   // 버튼명을 "장치 검색"로 변경
                        ((TextView) findViewById(R.id.deviceName)).setText("연결된 장치가 없습니다");
                        showMessage("[" + mDevice.getName()+"] 기기 연결 해제");             // [v0.03]
                        mState = UART_PROFILE_DISCONNECTED;
                        mService.close();

                        changeState(false);     // [v0.04]
                    }
                });
            }

            // 기기가 탐색됐을 때
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
            }

            // 아두이노로부터 받은 데이터 출력
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            text = text.replace(System.getProperty("line.separator"),"");       // 아스키코드 13 제거
                            text = text.substring(0,text.length()-1);                           // 아스키코드 10 제거

                            isOnOff = text;             // [v0.04]
                            changeButtonState();        // [v0.3] 장치 초깃값 변경
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }

            // UART 통신을 지원하지 않을 때
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
                showMessage("장치가 UART를 지원하지 않습니다. 연결 해제합니다.");
                mService.disconnect();
            }

        }
    };

    // 서비스 시작
    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    // 인텐트 필터 등록. 동적브로드캐스트 위해
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        unbindService(mServiceConnection);
        mService.stopSelf();
        mService = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
                    ((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - 연결중");         // 상단 텍스트 변경
                    mService.connect(deviceAddress);

                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    showMessage("블루투스가 켜졌습니다.");
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    showMessage("블루투스 연결을 재시도 해주십시오.");
                    finish();
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }


    @Override
    public void onBackPressed() {

        // [v0.6]
        // 블루투스 연결 상태에서 "뒤로가기" 클릭 시, 연결 유지
        if (mState == UART_PROFILE_CONNECTED) {
            Intent startMain = new Intent(this, Second_Activity.class);
            startMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(startMain);
        }
        // 종료 메시지 확인
        else {
            finish();
        }

//        showMessage("블루투스 연결이 해제됩니다.");
//        finish();
    }



    // 토스트 함수화
    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // [v0.04]
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.bt1_on:
            case R.id.bt1_off:
            {
                Send("w");          // 값 변경
                if( BT1_on.getVisibility() == View.VISIBLE){
                    BT1_on.setVisibility(View.GONE);
                    BT1_off.setVisibility(View.VISIBLE);
                }
                else{
                    BT1_on.setVisibility(View.VISIBLE);
                    BT1_off.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.bt2_on:
            case R.id.bt2_off:{
                Send("s");          // 값 변경
                if( BT2_on.getVisibility() == View.VISIBLE){
                    BT2_on.setVisibility(View.GONE);
                    BT2_off.setVisibility(View.VISIBLE);
                }
                else{
                    BT2_on.setVisibility(View.VISIBLE);
                    BT2_off.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.bt3_on:
            case R.id.bt3_off:{
                Send("x");          // 값 변경
                if( BT3_on.getVisibility() == View.VISIBLE){
                    BT3_on.setVisibility(View.GONE);
                    BT3_off.setVisibility(View.VISIBLE);
                }
                else{
                    BT3_on.setVisibility(View.VISIBLE);
                    BT3_off.setVisibility(View.GONE);
                }
                break;
            }


        }
    }

    // [v0.3] 장치 초깃값 변경
    public void changeButtonState() {
        if (isOnOff.equals("OFF")) {
            switch (tempData) {
                case "q":
                    Led_btn.setVisibility(View.GONE);
                    BT1_off.setVisibility(View.VISIBLE);
                    break;
                case "a":
                    Fume_btn.setVisibility(View.GONE);
                    BT2_off.setVisibility(View.VISIBLE);
                    break;
                case "z":
                    Fan_btn.setVisibility(View.GONE);
                    BT3_off.setVisibility(View.VISIBLE);
                    break;
            }
        }
        else {
            switch (tempData) {
                case "q":
                    Led_btn.setVisibility(View.GONE);
                    BT1_on.setVisibility(View.VISIBLE);
                    break;
                case "a":
                    Fume_btn.setVisibility(View.GONE);
                    BT2_on.setVisibility(View.VISIBLE);
                    break;
                case "z":
                    Fan_btn.setVisibility(View.GONE);
                    BT3_on.setVisibility(View.VISIBLE);
                    break;
            }
        }
        tempData = null;
    }


    // [v0.04] 블루투스가 연결/해제될 때 상태 변경
    public void changeState(boolean isBtConnect){
        if(isBtConnect){
            Led_btn.setEnabled(true);               // [v0.03] 연결되면 버튼 활성화
            Fume_btn.setEnabled(true);
            Fan_btn.setEnabled(true);

            getInitalData();             // [v0.6] 기기 상태 확인
        }
        else{
            BT1_on.setVisibility(View.GONE);        // [v0.3] 이미지 버튼 안보이게
            BT1_off.setVisibility(View.GONE);
            BT2_on.setVisibility(View.GONE);
            BT2_off.setVisibility(View.GONE);
            BT3_on.setVisibility(View.GONE);
            BT3_off.setVisibility(View.GONE);

            Led_btn.setVisibility(View.VISIBLE);
            Fume_btn.setVisibility(View.VISIBLE);
            Fan_btn.setVisibility(View.VISIBLE);

            Led_btn.setEnabled(false);              // [v0.03] 연결 해제시 버튼 비활성화
            Fume_btn.setEnabled(false);
            Fan_btn.setEnabled(false);
        }
    }

    // [v0.6] 초깃값 받아오기
    public void getInitalData(){

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //지연시키길 원하는 밀리초 뒤에 동작
                Send("q");          // LED 상태 확인
                tempData = "q";
            }
        }, 800);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Send("a");          // 공기청정기 상태 확인
                tempData = "a";
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Send("z");          // 선풍기 상태 확인
                tempData = "z";
            }
        }, 1200);

    }

}