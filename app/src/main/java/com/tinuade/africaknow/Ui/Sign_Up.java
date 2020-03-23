package com.tinuade.africaknow.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinuade.africaknow.Model.User;
import com.tinuade.africaknow.PlayGame;
import com.tinuade.africaknow.R;

public class Sign_Up extends AppCompatActivity {
    //widgets
    private EditText mFullname, mEmailAddress, mPhonenumber, mPassword, mConfirmPassword;
    private ProgressBar loadingProgressBar;
    //Firebase
    private FirebaseAuth mAuth;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseDatabase mFirebaseDatabase;



        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        users= mFirebaseDatabase.getReference("users");


        //findViewById for widgets
        loadingProgressBar = findViewById(R.id.loading);
        mFullname = findViewById(R.id.fullname);
        mEmailAddress = findViewById(R.id.sign_Up_email);
        mPhonenumber = findViewById(R.id.phoneNumber);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirm_password);
        TextView mSignInLink = findViewById(R.id.signInLink);
        Button mSignUpButton = findViewById(R.id.signup);

        mSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sign_Up.this, Sign_In.class);
                startActivity(intent);
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton();
            }
        });

    }

    private void signUpButton() {
        if (TextUtils.isEmpty(mFullname.getText().toString()) && mFullname.getText().toString().length() < 3) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mEmailAddress.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(mEmailAddress.getText().toString()).matches()) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString()) &&mEmailAddress.getText().toString().isEmpty()) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Password and Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mConfirmPassword.getText().toString()) && !mEmailAddress.getText().toString().contains("@")) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (!mPassword.getText().toString().equalsIgnoreCase(mConfirmPassword.getText().toString())) {
            Toast.makeText(Sign_Up.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            mPassword.setText("");
            mConfirmPassword.setText("");
        } else if (TextUtils.isEmpty(mPhonenumber.getText().toString()) && mPhonenumber.getText().toString().length() < 9) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Phone Number", Toast.LENGTH_LONG).show();
        } else
            //Register User
            loadingProgressBar.setVisibility(View.VISIBLE);

           mAuth.createUserWithEmailAndPassword(mEmailAddress.getText().toString(), mPassword.getText().toString())
                   .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                       @Override
                       public void onSuccess(AuthResult authResult) {
                           //save user to the database
                           User user = new User();
                           user.setFullName(mFullname.getText().toString());
                           user.setEmail(mEmailAddress.getText().toString());
                           user.setPhone(mPhonenumber.getText().toString());

                           //use phone as key

                           users.child(mAuth.getCurrentUser().getUid())
                                   .setValue(user)
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           Toast.makeText(Sign_Up.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(Sign_Up.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                       }
                                   });
                           loadingProgressBar.setVisibility(View.INVISIBLE);

                           startActivity(new Intent(Sign_Up.this, PlayGame.class));
                           finish();
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(Sign_Up.this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                       }
                   });

    }
}


