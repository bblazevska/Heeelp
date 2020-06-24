package com.example.heeelp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class ActiveService extends Service {

    protected static final int NOT_ID= 100;
    public static String TAG = "SERVICE";
    public static final String RESTART_INTENT = "com.example.heeelp.restarter";

    private static ActiveService currentService;
    private int timeCounter = 0;
    private static Timer timer;
    private TimerTask timerTask;
    private Context mContext;

    public ActiveService(Context context) {
        super();
        mContext=context;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            restartForeground();
        }
        currentService = this;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

        if(intent == null ){

            HelperServiceClass.launchService(this);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        startTimer();
        return START_STICKY;
    }

    private void startTimer() {
        Log.i(TAG , "Starting timer");
        stopTimerTask();
        timer = new Timer();

        initializeTimerTask();

        Log.i("SERVICE", "Scheduling...");
        timer.schedule(timerTask, 1000, 1000);
    }

    private void initializeTimerTask() {
        Log.i(TAG, "initialising TimerTask");
        timerTask = new TimerTask() {
            public void run() {
                Log.i(TAG, "in timer ++++  " + (timeCounter++));
            }
        };
    }

    private void stopTimerTask() {
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }


    private void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.d(TAG, " Restarting foreground!");
            try {
                ServiceNotification notification = new ServiceNotification();
                startForeground(NOT_ID,notification.setNotification(this, "Heeelp ", "This is the service's notification",R.drawable.ic_android));
                Log.i(TAG,"Restarting foreground successful");
                startTimer();
            }catch (Exception e){
                Log.e(TAG , "Error in notification " + e.getMessage());
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy is called");
        Intent broadcastIntent = new Intent(RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stopTimerTask();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved is called");
        Intent broadcastIntent =  new Intent(RESTART_INTENT);
        sendBroadcast(broadcastIntent);
    }
}
