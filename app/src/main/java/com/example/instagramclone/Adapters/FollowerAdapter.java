package com.example.instagramclone.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Models.FollowingModel;
import com.example.instagramclone.Models.FriendModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.FollowersSampleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {
   Context context;
   ArrayList<FriendModel> list;

    public FollowerAdapter(Context context, ArrayList<FriendModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.followers_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {

        FriendModel model=list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePhoto())
                                .placeholder(R.drawable.default_profile).into(holder.binding.userProfile);

                        holder.binding.username.setText(user.getName());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Log.d("Followed by",model.getFollowedBy());
         FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getFollowedBy())
                .child("followers")
                 .child(FirebaseAuth.getInstance().getUid())
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if (snapshot.exists())
                         {
                             holder.binding.followingBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_activebtn));
                             holder.binding.followingBtn.setText("Following");
                             holder.binding.followingBtn.setTextColor(context.getResources().getColor(R.color.black));
                             holder.binding.followingBtn.setEnabled(false);
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
        FollowersSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=FollowersSampleBinding.bind(itemView);

        }
    }
}
