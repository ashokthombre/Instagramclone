package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.instagramclone.Adapters.FollowerAdapter;
import com.example.instagramclone.Models.FriendModel;
import com.example.instagramclone.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity {

    ArrayList<FriendModel>list;
    RecyclerView followerRV;

    FirebaseDatabase database;
    FirebaseAuth auth;
   Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        followerRV=findViewById(R.id.followersRV);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FollowersActivity.this.setTitle("Followers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        list=new ArrayList<>();

//       list.add(new User("Ashoka"));
//       list.add(new User("Ashoka"));
//       list.add(new User("Ashoka"));
//       list.add(new User("Ashoka"));

        FollowerAdapter adapter=new FollowerAdapter(this,list);
        followerRV.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        followerRV.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users")
                .child(auth.getUid())
                .child("followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            FriendModel model=dataSnapshot.getValue(FriendModel.class);
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