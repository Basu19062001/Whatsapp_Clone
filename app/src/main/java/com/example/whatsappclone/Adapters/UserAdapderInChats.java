package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatsDetailsAcitivity;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapderInChats extends RecyclerView.Adapter<UserAdapderInChats.ViewHolder> {

    ArrayList<Users> list;
    Context context;

    public UserAdapderInChats(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample_for_chats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);
        //get image from online
        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.user).into(holder.imageView);
        holder.userName.setText(users.getUserName());

        //For last message
        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                holder.lastMessage.setText(dataSnapshot.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatsDetailsAcitivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profilePic", users.getProfilePic());
                intent.putExtra("userName", users.getUserName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameInChats);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
