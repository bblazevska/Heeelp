package com.example.heeelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            com.example.heeelp.ServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {

            HelperServiceClass.launchService(getApplicationContext());
        }
        finish();
    }
}
