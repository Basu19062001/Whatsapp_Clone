package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.Adapters.FragmentsAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //For view binding for ids
    ActivityMainBinding binding;
    //For authentication
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //object of FirebaseAuth class
        auth = FirebaseAuth.getInstance();

        //For viewPager
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        //set viewPager on tabLayout
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    //Set menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wp_clone_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Here worked of menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Toast.makeText(this, "Clicked on settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.groupChat:
                Intent groupChatIntent = new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(groupChatIntent);
                break;
        }
        return true;
    }


}