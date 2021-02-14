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

public class LogInActivity extends AppCompatActivity {
    private EditText password,email;
    private Button btnlogin;
    private TextView tv_register,error;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnlogin = findViewById(R.id.login);
        error = findViewById(R.id.error);
        tv_register = findViewById(R.id.tv_register);

        firebaseAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pwd = password.getText().toString();

                if(mail.isEmpty())
                {
                    error.setText("Please enter your email");
                }
                else if(pwd.isEmpty())
                {
                    error.setText("Please enter password");
                }
                else if (mail.isEmpty() && pwd.isEmpty())
                {
                    error.setText("Fields are empty!");
                }
                else if ( !(mail.isEmpty() && pwd.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(mail,pwd).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "User Logged In", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),MainScreen.class));
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(LogInActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });



    }
}