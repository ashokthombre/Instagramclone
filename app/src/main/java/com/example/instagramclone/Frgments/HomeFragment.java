package com.example.instagramclone.Frgments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.instagramclone.Adapters.PostAdapter;
import com.example.instagramclone.Adapters.StoryAdapter;
import com.example.instagramclone.Models.DashboardModel;
import com.example.instagramclone.Models.PostModel;
import com.example.instagramclone.Models.StoryModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.Models.UserStories;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
RecyclerView storyRv;
ShimmerRecyclerView dashboardRv;
ArrayList<StoryModel>list;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
ImageView userProfile;

RoundedImageView addStoryImage;
ArrayList<PostModel>modelArrayList;

ActivityResultLauncher<String> gallery;
ProgressDialog progressDialog;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("story uploading");
        progressDialog.setMessage("please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        storyRv=view.findViewById(R.id.storyRv);
        dashboardRv=view.findViewById(R.id.dashboardRv);
        userProfile=view.findViewById(R.id.userProfile);

        dashboardRv.showShimmerAdapter();


        list=new ArrayList<>();




        StoryAdapter adapter=new StoryAdapter(list,getContext());

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);

        storyRv.setLayoutManager(linearLayoutManager);
//       storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    list.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        StoryModel storyModel=new StoryModel();
                        storyModel.setStoryBy(dataSnapshot.getKey());
                        storyModel.setStoryAt(dataSnapshot.child("postedBy").getValue(Long.class));

                        ArrayList<UserStories> stories=new ArrayList<>();
                        for (DataSnapshot snapshot1:dataSnapshot.child("userStories").getChildren())
                        {
                            UserStories userStories=snapshot1.getValue(UserStories.class);
                            stories.add(userStories);


                        }
                        storyModel.setUserStories(stories);
                        list.add(storyModel);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



       modelArrayList=new ArrayList<>();

//       modelArrayList.add(new DashboardModel(R.drawable.default_profile,R.drawable.default_profile,R.drawable.save,"Ashoka Thombre","Student","244","254","255"));
//       modelArrayList.add(new DashboardModel(R.drawable.default_profile,R.drawable.default_profile,R.drawable.save,"Ashoka Thombre","Student","244","254","255"));
//       modelArrayList.add(new DashboardModel(R.drawable.default_profile,R.drawable.default_profile,R.drawable.save,"Ashoka Thombre","Student","244","254","255"));
//       modelArrayList.add(new DashboardModel(R.drawable.default_profile,R.drawable.default_profile,R.drawable.save,"Ashoka Thombre","Student","244","254","255"));

        PostAdapter dashboardAdapter=new PostAdapter(modelArrayList,getContext());


        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext());
        dashboardRv.setLayoutManager(linearLayoutManager1);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 modelArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    PostModel postModel=dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey());
                    modelArrayList.add(postModel);

                }
                dashboardRv.setAdapter(dashboardAdapter);
                dashboardRv.hideShimmerAdapter();
                dashboardAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        database.getReference().child("posts")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        modelArrayList.clear();
//                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
//                        {
//                            PostModel postModel=snapshot.getValue(PostModel.class);
//                            Toast.makeText(getContext(), ""+postModel.getPostedBy(), Toast.LENGTH_SHORT).show();
//
//                            modelArrayList.add(postModel);
//                            postModel.setPostId(dataSnapshot.getKey());
//
//
//                        }
//
//                   dashboardAdapter.notifyDataSetChanged();
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });



             database.getReference().child("Users").child(auth.getUid())
                     .addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             User user=snapshot.getValue(User.class);
                             Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile)
                                     .into(userProfile);

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });

             addStoryImage=view.findViewById(R.id.profileImage);

             addStoryImage.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                 gallery.launch("image/*");

                 }
             });

             gallery=registerForActivityResult(new ActivityResultContracts.GetContent(),
                     new ActivityResultCallback<Uri>() {
                         @Override
                         public void onActivityResult(Uri result) {

                             if (result==null)
                             {
                                 Toast.makeText(getContext(), "story not selected", Toast.LENGTH_SHORT).show();
                                 return;
                             }

                             addStoryImage.setImageURI(result);

                             progressDialog.show();

                             final StorageReference reference=storage.getReference().child("Stories")
                                     .child(auth.getUid())
                                     .child(new Date().getTime()+"");

                             reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                 @Override
                                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            StoryModel story=new StoryModel();
                                            story.setStoryAt(new Date().getTime());

                                            database.getReference().child("stories")
                                                    .child(auth.getUid()).child("postedBy")
                                                    .setValue(story.getStoryAt())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            UserStories stories=new UserStories(uri.toString(),story.getStoryAt());

                                                            database.getReference().child("stories")
                                                                    .child(auth.getUid())
                                                                    .child("userStories")
                                                                    .push()
                                                                    .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {

                                                                            progressDialog.dismiss();
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


       return view;
    }
}