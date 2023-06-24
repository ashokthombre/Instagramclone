package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagramclone.Adapters.FollowerAdapter;
import com.example.instagramclone.Adapters.FollowingAdapter;
import com.example.instagramclone.Models.FollowingModel;
import com.example.instagramclone.databinding.ActivityFollowingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {

    ActivityFollowingBinding binding;
    ArrayList<FollowingModel>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFollowingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        FollowingActivity.this.setTitle("Following");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=new ArrayList<>();


        FollowingAdapter adapter=new FollowingAdapter(this,list);
        binding.followingRV.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.followingRV.setLayoutManager(linearLayoutManager);


        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       for (DataSnapshot dataSnapshot:snapshot.getChildren())
                       {
                           FollowingModel model=dataSnapshot.getValue(FollowingModel.class);
                           list.add(model);

                       }
                       adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}