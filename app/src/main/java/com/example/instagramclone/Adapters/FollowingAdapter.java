package com.example.instagramclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Models.FollowingModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.FollowersSampleBinding;
import com.example.instagramclone.databinding.FollwingSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.Viewholder> {
    Context context;
    ArrayList<FollowingModel> list;

    public FollowingAdapter(Context context, ArrayList<FollowingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FollowingAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.follwing_sample, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.Viewholder holder, int position) {

        FollowingModel model = list.get(position);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getFollowingTo()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile).into(holder.binding.userProfile);
                        holder.binding.username.setText(user.getName());
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

    public class Viewholder extends RecyclerView.ViewHolder {
        FollwingSampleBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = FollwingSampleBinding.bind(itemView);
        }
    }
}
