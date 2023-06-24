package com.example.instagramclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Models.StoryModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.Models.UserStories;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.StoryrvDesignBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder>
{
    ArrayList<StoryModel> list;
    Context context;

    public StoryAdapter(ArrayList<StoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view= LayoutInflater.from(context).inflate(R.layout.storyrv_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {

        StoryModel story=list.get(position);
        if (story.getUserStories().size()>0) {
            UserStories lastStory = story.getUserStories().get(story.getUserStories().size() - 1);
            Picasso.get().load(lastStory.getImage()).into(holder.binding.profileImage);
            holder.binding.statusCircle.setPortionsCount(story.getUserStories().size());

            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Picasso.get().load(user.getProfilePhoto())
                                    .placeholder(R.drawable.default_profile)
                                    .into(holder.binding.userProfile);
                            holder.binding.name.setText(user.getName());
                            holder.binding.profileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ArrayList<MyStory> myStories = new ArrayList<>();

                                    for (UserStories stories : story.getUserStories()) {
                                        myStories.add(new MyStory(
                                                stories.getImage()

                                        ));
                                    }
                                    new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                            .setStoriesList(myStories) // Required
                                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                            .setTitleText(user.getName()) // Default is Hidden
                                            .setSubtitleText("") // Default is Hidden
                                            .setTitleLogoUrl(user.getProfilePhoto()) // Default is Hidden
                                            .setStoryClickListeners(new StoryClickListeners() {
                                                @Override
                                                public void onDescriptionClickListener(int position) {
                                                    //your action
                                                }

                                                @Override
                                                public void onTitleIconClickListener(int position) {
                                                    //your action
                                                }
                                            }) // Optional Listeners
                                            .build() // Must be called before calling show method
                                            .show();
                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        StoryrvDesignBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding=StoryrvDesignBinding.bind(itemView);


        }
    }
}
