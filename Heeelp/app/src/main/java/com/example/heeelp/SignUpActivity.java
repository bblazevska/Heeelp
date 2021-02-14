package com.example.heeelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText name,username,password;
    private Button btnsignup;
    private TextView textView,error;

    FirebaseAuth mFirebaseAuth;


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
        error = findViewById(R.id.error);

        if(mFirebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String pwd = password.getText().toString();

               // Toast.makeText(SignUpActivity.this,"Registration unsuccessful,Please try again!",Toast.LENGTH_LONG).show();


                if(email.isEmpty())
                {
                    error.setText("Please enter your email");
                }
                else if(pwd.isEmpty())
                {
                    error.setText("Please enter password");
                }
                else if (email.isEmpty() && pwd.isEmpty())
                {
                    error.setText("Fields are empty!");
                }
                else if ( !(email.isEmpty() && pwd.isEmpty()))
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"User Created", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                
                            }
                            else{
                                Toast.makeText(SignUpActivity.this,"Error!"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

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