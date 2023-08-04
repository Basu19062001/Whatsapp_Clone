package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.example.whatsappclone.Adapters.ChatAdapter;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.databinding.ActivityChatsDetailsAcitivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatsDetailsAcitivity extends AppCompatActivity {

    ActivityChatsDetailsAcitivityBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsDetailsAcitivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        //Objects
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        //get info from intent
        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        //Set info
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.profileImage);

        //For backArrow button
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModelArrayList, this, receiverId);

        binding.chatsRecyclerView.setAdapter(chatAdapter);

        binding.chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //sender child
        final String senderRoom = senderId + receiverId;
        //receiver child
        final String receiverRoom = receiverId + senderId;

        //Retrieve messages from firebase database
        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel chatsmodel = dataSnapshot.getValue(MessageModel.class);
                            chatsmodel.setMessageUserId(dataSnapshot.getKey());
                            messageModelArrayList.add(chatsmodel);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //For chatting
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.edtMessage.getText().toString();
                final MessageModel messageModel = new MessageModel(senderId, message);
                messageModel.setTimestamp(new Date().getTime());
                binding.edtMessage.setText("");

                //set messages into the firebase database
                database.getReference()
                        .child("Chats")
                        .child(senderRoom)
                        .push() //For every message
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //For receiver
                                database.getReference().child("Chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });

            }
        });


    }
}