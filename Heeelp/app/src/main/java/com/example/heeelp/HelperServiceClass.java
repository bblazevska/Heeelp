package com.example.heeelp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class HelperServiceClass {

    private static String TAG = "HelperClass";

    public HelperServiceClass() {
    }

    public static void launchService(Context context) {
        if (context == null) {
            return;
        }
        Intent serviceIntent = new Intent(context,ActiveService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
        Log.d(TAG, "From HelperServiceClass : Service can start!");
    }
}
