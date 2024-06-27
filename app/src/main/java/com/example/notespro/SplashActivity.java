package com.example.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // it handle for delay of animation icon loading
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // it determine the credentials of user through firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


                if(currentUser==null){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish(); // it exit the program
            }
        }, 1000); // it delay 1 second
    }
}