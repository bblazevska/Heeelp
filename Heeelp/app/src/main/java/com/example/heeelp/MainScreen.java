package com.example.heeelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {
    LocationManager locationManager;
    private Button sendLocation;
//    Location latitude;
//    Location longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main_screen);

         sendLocation = findViewById(R.id.sendLocbtn);

         sendLocation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               LocationTracker  locationTracker = new LocationTracker(MainScreen.this);
               if(locationTracker.canGetLocation()) {
                   double latitude = locationTracker.getLatitude();
                   double longitude = locationTracker.getLongitude();
                   Toast.makeText(getApplicationContext(), "Your location is \n" +
                           "Lat: " + latitude + " Long: " + longitude, Toast.LENGTH_LONG).show();
               }else{
                   locationTracker.showAlert();
               }


             }
         });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}