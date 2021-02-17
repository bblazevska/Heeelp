package com.example.heeelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserScreen extends AppCompatActivity {
    LocationTracker  locationTracker;
    private TextView wellcomeUser;
    private Button sendLocation, logOut;
    private Location location;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    private RequestQueue requestQueue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("heeelp");

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
                Toast.makeText(UserScreen.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserScreen.this, LogInActivity.class));
            }
        });

        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationTracker = new LocationTracker(UserScreen.this);
                if(locationTracker.canGetLocation()) {
                    double latitude = locationTracker.getLatitude();
                    double longitude = locationTracker.getLongitude();
                    location = locationTracker.getLocation();
                    Toast.makeText(getApplicationContext(), "Your location is \n" +
                            "Lat: " + latitude + " Long: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    locationTracker.showAlert();
                }

                sendNotification();

            }
        });
    }

    private void sendNotification(){
        JSONObject json = new JSONObject();

        try {
            json.put("to","/topics/"+"heeelp");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Send Help!");
            notificationObj.put("body","My location is:  " + location);

//            JSONObject extraData = new JSONObject();
//            extraData.put("brandId","puma");
//            extraData.put("category","Shoes");

            json.put("notification",notificationObj);
//            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            )
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAEf4TKFw:APA91bEkxDxKBEYcZzudAFShkn6-TO9Tx_7VRCi0zN0XpzzXbTILUVAxJsSeUiliw-XqdEDuAmgcWGf_62r5IJYB5XPxe9gXgtepuPWee_wMA2JYQW0fmzslfJKevDTgKcbtc-wJO035");
                    return header;
                }
            };
            requestQueue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }

    }
}