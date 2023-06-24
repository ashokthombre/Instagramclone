package com.example.instagramclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Models.FriendModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.FriendsRvSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
   Context context;
   ArrayList<FriendModel>list;

    public FriendAdapter(Context context, ArrayList<FriendModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.friends_rv_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {

        FriendModel model=list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePhoto())
                                .placeholder(R.drawable.default_profile).into(holder.binding.userProfile);

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

        FriendsRvSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           binding=FriendsRvSampleBinding.bind(itemView);
        }
    }
}
