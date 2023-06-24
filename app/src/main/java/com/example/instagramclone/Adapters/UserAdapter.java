package com.example.instagramclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Models.FollowingModel;
import com.example.instagramclone.Models.FriendModel;
import com.example.instagramclone.Models.NotificationModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
Context context;
ArrayList<User>list;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.user_sample,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        User user=list.get(position);
        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile).into(holder.profile);

        holder.name.setText(user.getName());
        holder.profession.setText(user.getProfession());

        FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(user.getUserId())
                                .child("followers")
                                           .child(FirebaseAuth.getInstance().getUid())

                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists())
                                                        {
                                                            holder.follow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_activebtn));
                                                            holder.follow.setText("Following");
                                                            holder.follow.setTextColor(context.getResources().getColor(R.color.black));
                                                            holder.follow.setEnabled(false);

                                                        }
                                                        else
                                                        {
                                                            holder.follow.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {

                                                                    FriendModel model=new FriendModel();
                                                                    model.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                                                    model.setFollowedAt(new Date().getTime());

                                                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                                                            .child(user.getUserId())
                                                                            .child("followers")
                                                                            .child(FirebaseAuth.getInstance().getUid()).setValue(model)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {

                                                                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                                                                            .child(user.getUserId())
                                                                                            .child("followerCount")
                                                                                            .setValue(user.getFollowerCount()+1)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {

                                                                                                    holder.follow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_activebtn));
                                                                                                    holder.follow.setText("Following");
                                                                                                    holder.follow.setTextColor(context.getResources().getColor(R.color.black));
                                                                                                    holder.follow.setEnabled(false);

                                                                                                    Toast.makeText(context, "You Followed "+user.getName(), Toast.LENGTH_SHORT).show();

                                                                                                    NotificationModel notificationModel=new NotificationModel();
                                                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                                                    notificationModel.setNotificationAt(new Date().getTime()+"");
                                                                                                    notificationModel.setType("follow");
                                                                                                    notificationModel.setCheckOpen("false");

                                                                                                    FirebaseDatabase.getInstance().getReference().child("notification")
                                                                                                            .child(user.getUserId())
                                                                                                            .push()
                                                                                                            .setValue(notificationModel);

                                                                                                      //Following....

                                                                                                    FollowingModel followingModel=new FollowingModel();
                                                                                                    followingModel.setFollwingAt(new Date().getTime()+"");
                                                                                                    followingModel.setFollowingTo(user.getUserId());

                                                                                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                                                                                            .child(FirebaseAuth.getInstance().getUid())
                                                                                                            .child("following")
                                                                                                            .child(user.getUserId())
                                                                                                            .setValue(followingModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void unused) {
                                                                                                                   FirebaseDatabase.getInstance().getReference()
                                                                                                                           .child("Users")
                                                                                                                           .child(FirebaseAuth.getInstance().getUid())
                                                                                                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                               @Override
                                                                                                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                   User user1=snapshot.getValue(User.class);
                                                                                                                                   FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                                                                                                           .child("followingCount").setValue(user1.getFollowingCount()+1);

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
                                                                            });

                                                                }
                                                            });


                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });







    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name,profession;
        AppCompatButton follow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile=itemView.findViewById(R.id.profilePic);
            name=itemView.findViewById(R.id.name);
            profession=itemView.findViewById(R.id.profession);

            follow=itemView.findViewById(R.id.btnfollow);

        }
    }
}
