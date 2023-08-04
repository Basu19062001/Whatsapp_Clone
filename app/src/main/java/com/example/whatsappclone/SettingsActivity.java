package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    //Binding
    ActivitySettingsBinding binding;

    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hiding toolbar
        getSupportActionBar().hide();

        //Instance
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //For back arrow
        binding.backArrowSettingsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Privacy Policy", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "About Us", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Help is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Notification", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtInviteAFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Invite a friend", Toast.LENGTH_SHORT).show();
            }
        });

        //For save user name and about
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = binding.edtUserName.getText().toString();
                String about = binding.edtStatus.getText().toString();

                //Update value on firebase database
                HashMap<String,Object> obj = new HashMap<>();
                obj.put("userName",userName);
                obj.put("about",about);

                //insert data into database
                database.getReference().child("Users")
                        .child(auth.getUid())
                        .updateChildren(obj);
                Toast.makeText(SettingsActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
            }
        });


        //Image get from firebase
        database.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilePic())
                                .placeholder(R.drawable.user)
                                .into(binding.profileImage);

                        //set into settingsActivity
                        binding.edtUserName.setText(users.getUserName());
                        binding.edtStatus.setText(users.getAbout());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Upload image on firebase
        //We need to storage of firebase. We add dependency of storage of firebase
        binding.plusForAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Settings to gallery
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null) {    //If user select a image

            Uri sFile = data.getData();
            binding.profileImage.setImageURI(sFile);

            //Here image is storing into firebase storage
            final StorageReference reference = storage.getReference().child("Profile_pictures")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic")
                                    .setValue(uri.toString());
                            Toast.makeText(SettingsActivity.this, "Profile pic updated", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });
        }
    }
}