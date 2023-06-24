package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.instagramclone.Adapters.CommentAdapter;
import com.example.instagramclone.Models.Comment;
import com.example.instagramclone.Models.NotificationModel;
import com.example.instagramclone.Models.PostModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    String postId;
    String postedBy;
    Intent intent;
    ArrayList<Comment>commentArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

     setSupportActionBar(binding.toolbar3);
     CommentActivity.this.setTitle("Comments");
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);



      intent= getIntent();

      postId=intent.getStringExtra("postId");
      postedBy=intent.getStringExtra("postedBy");

      FirebaseDatabase.getInstance().getReference().child("Users").child(postedBy)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile).into(binding.userProfile);
                        binding.username.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PostModel postModel=snapshot.getValue(PostModel.class);
                Picasso.get().load(postModel.getPostImage()).placeholder(R.drawable.default_profile).into(binding.postImage);
                binding.description.setText(postModel.getPostDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        commentArrayList=new ArrayList<>();

        CommentAdapter commentAdapter=new CommentAdapter(this,commentArrayList);
        binding.commentRV.setAdapter(commentAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.commentRV.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference().child("posts").child(postId).child("Comments")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               commentArrayList.clear();
                                for (DataSnapshot dataSnapshot :snapshot.getChildren())
                                {
                                    Comment comment=dataSnapshot.getValue(Comment.class);
                                    commentArrayList.add(comment);
                                }
                                commentAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment=binding.comment.getText().toString();
                if (comment.isEmpty())
                {
                    return;
                }

                Comment comment1=new Comment();
                comment1.setCommentBody(comment);
                comment1.setCommentedAt(new Date().getTime());
                comment1.setCommentedBy(FirebaseAuth.getInstance().getUid());

                FirebaseDatabase.getInstance().getReference().child("posts").child(postId)
                        .child("Comments").push().setValue(comment1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                           FirebaseDatabase.getInstance().getReference().child("posts")
                                   .child(postId)
                                   .child("commentCount")
                                   .addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                           int commentCount=0;
                                           if (snapshot.exists())
                                           {
                                               commentCount=snapshot.getValue(Integer.class);
                                           }
                                           FirebaseDatabase.getInstance().getReference()
                                                   .child("posts")
                                                   .child(postId).child("commentCount")
                                                   .setValue(commentCount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           binding.comment.setText("");
                                                           Toast.makeText(CommentActivity.this, "commented successfully.", Toast.LENGTH_SHORT).show();

                                                           NotificationModel notificationModel=new NotificationModel();
                                                           notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                           notificationModel.setNotificationAt(new Date().getTime()+"");
                                                           notificationModel.setPostId(postId);
                                                           notificationModel.setPostedBy(postedBy);
                                                           notificationModel.setType("comment");
                                                           notificationModel.setCheckOpen("false");


                                                           FirebaseDatabase.getInstance().getReference()
                                                                   .child("notification")
                                                                   .child(postedBy).push().setValue(notificationModel);

                                                       }
                                                   });
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error) {

                                       }
                                   });

                            }
                        });



            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       finish();
        return super.onOptionsItemSelected(item);
    }
}