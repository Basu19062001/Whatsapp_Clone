package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class SignInActivity extends AppCompatActivity {

    //for view binding for ids
    ActivitySignInBinding binding;
    ///For loading
    ProgressDialog progressDialog;
    //for authentication
    FirebaseAuth auth;
  //For Google Sign in
    GoogleSignInClient googleSignInClient;
    //For database
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For view binding for ids
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //For toolbar hide
        getSupportActionBar().hide();

        //Object of ProgressDialog class for loading
        progressDialog = new ProgressDialog(this);
        //Object of FirebaseAuth class
        auth = FirebaseAuth.getInstance();
        //Object of database
        database = FirebaseDatabase.getInstance();
        //For loading
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");

        binding.btnSignInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignInActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        });

       //For google sign In
        // Initialize sign in options the client-id is copied form google-services.json file
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("49159876918-1jjd2v6ua81emd23arfn75b9lenisjtu.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);

      //For google signIn
        binding.btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                // Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent, 100);
            }
        });

        //onClick on signIn button
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.edtSignInEmailPhone.getText().toString().isEmpty()){
                    binding.edtSignInEmailPhone.setError("Enter your email");
                    return;
                }
                if (binding.edtSignInPassword.getText().toString().isEmpty()){
                    binding.edtSignInPassword.setError("Enter your password");
                    return;
                }
                progressDialog.show();

                //sign in from authentication user
                auth.signInWithEmailAndPassword(binding.edtSignInEmailPhone.getText().toString()
                                , binding.edtSignInPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //Intent SignIn to SignUp activity
        binding.txtSignInClickForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        //Already a user logged
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

  //For google signIn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        auth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressDialog.dismiss();
                                        // Check condition
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = auth.getCurrentUser();
                                            Users userModel = new Users();
                                            userModel.setProfilePic(user.getPhotoUrl().toString());
                                            userModel.setUserId(user.getUid());
                                            userModel.setUserName(user.getDisplayName());
                                            userModel.setUserEmail(user.getEmail());
                                            database.getReference().child("Users").child(user.getUid()).setValue(userModel);

                                            // When task is successful redirect to profile activity display Toast
                                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                            displayToast("Firebase authentication successful");
                                        } else {
                                            // When task is unsuccessful display Toast
                                            displayToast("Authentication Failed :" + task.getException().getMessage());
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}


