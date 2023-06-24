package com.example.instagramclone.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.CommentActivity;
import com.example.instagramclone.Models.NotificationModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.NotificationSampleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<NotificationModel>list;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_sample,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        NotificationModel model=list.get(position);

        String type=model.getType();

           FirebaseDatabase.getInstance().getReference()
                   .child("Users")
                   .child(model.getNotificationBy())
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                           User user=snapshot.getValue(User.class);
                           Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile)
                                   .into(holder.binding.userProfile);

                           if (type.equals("like"))
                           {
                               holder.binding.notificationText.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+"  "+ "Liked your photo"));
                           } else if (type.equals("comment")) {
                               holder.binding.notificationText.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+"  "+ "Mentioned you in comment"));
                           }
                           else
                           {
                               holder.binding.notificationText.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+"  "+ "Started following you"));
                           }


                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
           holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (!type.equals("follow"))
                   {
                       FirebaseDatabase.getInstance().getReference()
                                       .child("notification")
                                               .child(model.getPostedBy())
                                                       .child(model.getNotificationId())
                                                               .child("checkOpen")
                                                                       .setValue("true");

                       holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                       Intent intent=new Intent(context,CommentActivity.class);
                       intent.putExtra("postId",model.getPostId());
                       intent.putExtra("postedBy",model.getPostedBy());
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       context.startActivity(intent);
                   }

               }
           });

           String checkOpen= model.getCheckOpen();
        {
            if (checkOpen.equals("true"))
            {
                holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else
            {

            }
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NotificationSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=NotificationSampleBinding.bind(itemView);


        }
    }
}
