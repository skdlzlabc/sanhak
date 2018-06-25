package com.scv.scv_project;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;

public class NotiService extends Service {
    private final String API_KEY = "A1E-eff520208aefbc57a8d1bee6a7d468c851a9"; // API KEY 값
    private final String VARIABLE_ID1 = "5a5c88b0c03f9779be224d28"; // value ID1 미세먼지
    private final String VARIABLE_ID2 = "5a97d545c03f971038188d8e"; // value ID2 습도
    private final String VARIABLE_ID3 = "5a97f71dc03f97388b2635e1"; // value ID3 온도
    private Thread th1=null;
    Thread notiTh =null;
    private Value[] var1, var2, var3;
    Integer tem1,tem2,tem3;
    public int onStartCommand(Intent i, int flag, int startId ){

        if(th1==null){
            th1=new Thread("for notification")
            {
                public void run(){
                    while(true){
                        ApiClient apiClient = new ApiClient(API_KEY);
                        Variable batteryLevel1 = apiClient.getVariable(VARIABLE_ID1);
                        Variable batteryLevel2 = apiClient.getVariable(VARIABLE_ID2);
                        Variable batteryLevel3 = apiClient.getVariable(VARIABLE_ID3);
                        var1=batteryLevel1.getValues();
                        tem1=(int)var1[0].getValue();
                        var2=batteryLevel2.getValues();
                        tem2=(int)var2[0].getValue();
                        var3=batteryLevel3.getValues();
                        tem3=(int)var3[0].getValue();

                        try{Thread.sleep(2000);}
                        catch(Exception e){}

                            if (tem1>80) {
                                Runnable r =new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent in = new Intent(NotiService.this, Second_Activity.class);
                                        PendingIntent pi = PendingIntent.getActivity(NotiService.this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Notification nn = new NotificationCompat.Builder(NotiService.this)
                                                .setContentTitle("집에 미세먼지가 심해요!")
                                                .setContentText("공기청정기를 가동해주세요")
                                                .setSmallIcon(R.drawable.icon)
                                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                                                .setContentIntent(pi).build();



                                        startForeground(123, nn);
                                    }
                                };
                                notiTh=new Thread(r);
                                notiTh.start();
                            }
                            if (tem2<=30) {
                                Runnable r =new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent in = new Intent(NotiService.this, Second_Activity.class);
                                        PendingIntent pi = PendingIntent.getActivity(NotiService.this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Notification nn = new NotificationCompat.Builder(NotiService.this)
                                                .setContentTitle("집이 너무 건조해요!")
                                                .setContentText("가습기를 가동해주세요")
                                                .setSmallIcon(R.drawable.icon)
                                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                                                .setContentIntent(pi).build();



                                        startForeground(123, nn);
                                    }

                                };
                                notiTh=new Thread(r);
                                notiTh.start();
                            }
                            if (tem3>=29 ) {
                                Runnable r =new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent in = new Intent(NotiService.this, Second_Activity.class);
                                        PendingIntent pi = PendingIntent.getActivity(NotiService.this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Notification nn = new NotificationCompat.Builder(NotiService.this)
                                                .setContentTitle("집이 더워요!")
                                                .setContentText("에어컨을 가동해주세요")
                                                .setSmallIcon(R.drawable.icon)
                                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                                                .setContentIntent(pi).build();



                                        startForeground(123, nn);
                                    }

                                };
                                notiTh=new Thread(r);
                                notiTh.start();
                            }



                    }
                }
            };
            th1.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
