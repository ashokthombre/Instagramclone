package com.example.instagramclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.CommentActivity;
import com.example.instagramclone.Models.DashboardModel;
import com.example.instagramclone.Models.NotificationModel;
import com.example.instagramclone.Models.PostModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    ArrayList<PostModel>dashboardModels;
    Context context;

    public PostAdapter(ArrayList<PostModel> dashboardModels, Context context) {
        this.dashboardModels = dashboardModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.dashboardrv_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

      PostModel model=dashboardModels.get(position);
        Picasso.get().load(model.getPostImage()).placeholder(R.drawable.default_profile).into(holder.postImage);

        String description=model.getPostDescription();

        if (description.equals(""))
        {
            holder.description.setVisibility(View.GONE);
        }
        else
        {
            holder.description.setText(description);
        }
        holder.like.setText(model.getPostLike()+"");
        holder.comment.setText(model.getCommentCount()+"");



        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePhoto())
                                .placeholder(R.drawable.default_profile)
                                .into(holder.profile);
                        holder.name.setText(user.getName());
                         holder.about.setText(user.getProfession());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference()
                        .child("posts")
                                .child(model.getPostId())
                                        .child("likes")
                                                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart,0,0,0);
                        }
                        else
                        {
                            holder.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(model.getPostId())
                                                            .child("postLike")
                                                            .setValue(model.getPostLike() +1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {

                                                                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart,0,0,0);


                                                                    NotificationModel notificationModel=new NotificationModel();
                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationModel.setNotificationAt(new Date().getTime()+"");
                                                                    notificationModel.setPostId(model.getPostId());
                                                                    notificationModel.setPostedBy(model.getPostedBy());
                                                                    notificationModel.setType("like");
                                                                    notificationModel.setCheckOpen("false");


                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(model.getPostedBy())
                                                                            .push()
                                                                            .setValue(notificationModel);


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

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("postId",model.getPostId());
                intent.putExtra("postedBy",model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return dashboardModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile,postImage,save;
        TextView name,about,like,comment,share,description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile=itemView.findViewById(R.id.userProfile);
            postImage=itemView.findViewById(R.id.addPost);
            save=itemView.findViewById(R.id.save);
            name=itemView.findViewById(R.id.userName);
            about=itemView.findViewById(R.id.about);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            share=itemView.findViewById(R.id.share);
            description=itemView.findViewById(R.id.description);
        }
    }

}
