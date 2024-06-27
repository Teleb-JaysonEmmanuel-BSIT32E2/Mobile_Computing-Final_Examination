package com.example.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // declare variables
    EditText txtEmail, txtPassword;
    Button btnLogin;
    ProgressBar progressBar;
    TextView lblRegisterAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // the ids from xml file are swap in this new variables
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progress_bar);
        lblRegisterAccount = findViewById(R.id.lblRegisterAcc);

        btnLogin.setOnClickListener((v) -> loginUser() );

        // when the user click register acc then this code will be shown the create account form
        lblRegisterAccount.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

    }

    void loginUser(){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        boolean isValidated = validateData(email, password);

        if(!isValidated){
            return;
        }

        loginAccountInFireBase(email, password);
    }

    void loginAccountInFireBase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    // login successfully
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){

                        //go to main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else{
                        Utility.showToast(LoginActivity.this, "Email not verified, Please verify your email.");
                    }
                }
                else{
                    //login failed
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }

    }

    boolean validateData(String email, String password){
        // validate the data that are input by user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Your email are invalid");
            return false;
        }

        if(password.length()<8){
            txtPassword.setError("Your password must more than 8 characters");
            return false;
        }

        return true;
    }
}