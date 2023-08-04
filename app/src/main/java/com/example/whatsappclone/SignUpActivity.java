package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    //ViewBinding for ids
    ActivitySignUpBinding binding;

    private FirebaseAuth auth;
    FirebaseDatabase database;
    ///for loading
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view binding for ids
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //for hide toolbar
        getSupportActionBar().hide();

        //object of FirebaseAuth class, we use for signup with email and password
        auth = FirebaseAuth.getInstance();
        //object of FirebaseDatabase class
        database = FirebaseDatabase.getInstance();
        //object of ProgressDialog class for loading
        progressDialog = new ProgressDialog(this);

        //for loading
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("We're creating your account");

        //onClick listener on sign up button
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtSignUpUserName.getText().toString().isEmpty()){
                    binding.edtSignUpUserName.setError("Enter your name");
                    return;
                }
                if (binding.edtSignUpEmail.getText().toString().isEmpty()){
                    binding.edtSignUpEmail.setError("Enter your Email");
                    return;
                }
                if (binding.edtSignUpPassword.getText().toString().isEmpty()){
                    binding.edtSignUpPassword.setError("Fill password");
                    return;
                }
                //for loading
                progressDialog.show();

                //we will set here Email and password in authentication
                auth.createUserWithEmailAndPassword(
                        binding.edtSignUpEmail.getText().toString(),
                        binding.edtSignUpPassword.getText().toString()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //for loading
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            //for user data we will store in realtime database
                            Users user = new Users(binding.edtSignUpUserName.getText().toString()
                                                    ,binding.edtSignUpEmail.getText().toString()
                                                    ,binding.edtSignUpPassword.getText().toString());
                            //get userId from authentication
                            String id = task.getResult().getUser().getUid();
                            //set data into realtime database
                            database.getReference().child("Users").child(id).setValue(user);

                            Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        binding.txtSignUpAlreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}