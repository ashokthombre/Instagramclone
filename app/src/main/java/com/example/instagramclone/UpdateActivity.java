package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.instagramclone.databinding.ActivityUpdateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    ActivityUpdateBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        setSupportActionBar(binding.toolbar2);
        UpdateActivity.this.setTitle("Update..");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=binding.userName.getText().toString();

                String profession=binding.profession.getText().toString();

                String about=binding.about.getText().toString();

                Map<String,Object> obj=new HashMap<>();
                obj.put("name",username);
                obj.put("profession",profession);
                obj.put("About",about);

                database.getReference().child("Users").child(auth.getUid())
                        .updateChildren(obj);

                Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}