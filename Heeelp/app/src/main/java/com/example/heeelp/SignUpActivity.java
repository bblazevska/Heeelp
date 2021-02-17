package com.example.heeelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heeelp.data.LoginRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText name,username,password;
    private Button btnsignup;
    private TextView textView;

    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnsignup = findViewById(R.id.signup);
        textView = findViewById(R.id.textView);


        if(mFirebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = username.getText().toString();
                final String pwd = password.getText().toString();
                final String fullName = name.getText().toString();
               // Toast.makeText(SignUpActivity.this,"Registration unsuccessful,Please try again!",Toast.LENGTH_LONG).show();

                if(fullName.isEmpty()){
                    name.setError("Full Name is required!");
                    name.requestFocus();
                    return;
                }
                if(email.isEmpty())
                {
                    username.setError("Email is required!");
                    username.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    username.setError("Please provide valid email!");
                    username.requestFocus();
                    return;
                }

                if(pwd.isEmpty())
                {
                    password.setError("Password is required!");
                    password.requestFocus();
                    return;
                }
                if (pwd.length() <6){
                    password.setError("Min password length should be 6 characters!");
                    password.requestFocus();
                    return;
                }


                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(fullName,email);


                                firebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(SignUpActivity.this,"User created successfully!", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                                }else{
                                                    Toast.makeText(SignUpActivity.this,"Failed to create user!"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(SignUpActivity.this,"Failed to create user!"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogInActivity.class));
            }
        });


    }
}