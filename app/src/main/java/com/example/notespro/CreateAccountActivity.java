package com.example.notespro;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class CreateAccountActivity extends AppCompatActivity {

    // declare handle variables
    EditText txtEmail, txtPassword, txtConfirmPassword;
    Button btnCreateAccount;
    ProgressBar progressBar;
    TextView lblLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // the ids from xml file are swap in this new variables
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);
        progressBar = findViewById(R.id.progress_bar);
        lblLogin = findViewById(R.id.lblLogin);

        // when the user click button create account, it calls the code
        btnCreateAccount.setOnClickListener(v -> createAccount());

        // return program
        lblLogin.setOnClickListener(v -> finish());
    }

    void createAccount(){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);

        if(!isValidated){
            return;
        }

        createAccountInFireBase(email, password);

    }

    void createAccountInFireBase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            // account successfully created

                            // it shows dialog box valid credentials
                            Utility.showToast(CreateAccountActivity.this, "Successfully create account, check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish(); // it exit the program
                        }
                        else{
                            // failed create account

                            // it shows dialog box error
                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            btnCreateAccount.setVisibility(View.VISIBLE);
        }

    }

    boolean validateData(String email, String password, String confirmPassword){
        // validate the data that are input by user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Your email are invalid");
            return false;
        }

        if(password.length()<8){
            txtPassword.setError("Your password must more than 8 characters");
            return false;
        }

        if(!password.equals(confirmPassword)){
            txtConfirmPassword.setError("Your password and confirm password are not matched");
            return false;
        }

        return true;
    }
}