package com.example.heeelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private EditText password,email;
    private Button btnlogin;
    private TextView tv_register;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnlogin = findViewById(R.id.login);

        tv_register = findViewById(R.id.tv_register);

        firebaseAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                if(mail.isEmpty())
                {
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    email.setError("Please enter a valid email!");
                    email.requestFocus();
                    return;
                }
                if(pwd.isEmpty())
                {
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }
                if(pwd.length() < 6 ){
                    password.setError("Min password length is 6 characters!");
                    password.requestFocus();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(mail,pwd).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "User Logged In", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LogInActivity.this, UserScreen.class));
//                                Log.d("From Log In Activity  :", "Starting Main activity");
                            } else {
                                Toast.makeText(LogInActivity.this, "Can't log in user!" , Toast.LENGTH_LONG).show();
                            }
                        }
                });
                }

        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
            }
        });



    }
}