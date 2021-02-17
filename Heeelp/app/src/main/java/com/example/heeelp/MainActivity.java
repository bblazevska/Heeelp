package com.example.heeelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    LocationTracker  locationTracker;
    private TextView wellcomeUser;
    private Button sendLocation, logOut;
    Context mContext;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(" from Main Activity", "  ACTIVITY STARTED");
        mContext = this;
        sendLocation = findViewById(R.id.sendLocbtn);
        logOut = findViewById(R.id.logOut);
        wellcomeUser = findViewById(R.id.wellcomeUser);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProf = snapshot.getValue(User.class);

                if(userProf != null){
                    String fullName = userProf.fullName;

                    wellcomeUser.setText("Welcome, " + fullName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        });

        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationTracker = new LocationTracker(MainActivity.this);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            com.example.heeelp.ServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {

            HelperServiceClass.launchService(getApplicationContext());
        }
        finish();
    }
}
