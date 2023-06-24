package com.example.instagramclone.Frgments;

import static android.app.Activity.RESULT_CANCELED;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagramclone.Adapters.FriendAdapter;
import com.example.instagramclone.FollowersActivity;
import com.example.instagramclone.FollowingActivity;
import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.Models.FriendModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.example.instagramclone.UpdateActivity;
import com.example.instagramclone.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {


    ArrayList<FriendModel> friendModels;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    Dialog dialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Updating Image..");
        progressDialog.setMessage("please wait..");
        dialog = new Dialog(getContext());
    }

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.inflate(inflater, container, false);

//        friendRv=view.findViewById(R.id.friendRv);

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatButton update, logout;
                dialog.setContentView(R.layout.sample_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                update = dialog.findViewById(R.id.update);
                logout = dialog.findViewById(R.id.logout);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), UpdateActivity.class);
                        startActivity(intent);


                    }
                });
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.signOut();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(intent);
                        getActivity().finish();

                    }
                });

                dialog.show();


            }
        });

        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            User user = snapshot.getValue(User.class);
                            binding.nameText.setText(user.getName());
                            binding.professionText.setText(user.getProfession());

                            binding.aboutText.setText(user.getAbout());

                            binding.followers.setText(user.getFollowerCount() + "");
                            binding.follows.setText(user.getFollowingCount() + "");
                            binding.photos.setText("1");

                            Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile).into(binding.userProfile);

                            Picasso.get().load(user.getCoverPhoto()).placeholder(R.drawable.default_profile).into(binding.coverImage);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        friendModels = new ArrayList<>();


        FriendAdapter adapter = new FriendAdapter(getContext(), friendModels);

        binding.friendRv.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.friendRv.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users")
                .child(auth.getUid())
                .child("followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        friendModels.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FriendModel model = dataSnapshot.getValue(FriendModel.class);
                            friendModels.add(model);

                        }
                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);

            }
        });

        binding.plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });


        binding.followerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FollowersActivity.class);
                getContext().startActivity(intent);
            }
        });

        binding.textView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowingActivity.class);
                getContext().startActivity(intent);
            }
        });


        return binding.getRoot();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 100) {
                if (data.getData() != null) {

                    Uri uri = data.getData();
                    binding.coverImage.setImageURI(uri);
                    progressDialog.show();
                    final StorageReference reference = storage.getReference().child("cover_photo").child(auth.getUid());

                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getContext(), "Image Uploades", Toast.LENGTH_SHORT).show();
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    database.getReference().child("Users").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
            }

            if (requestCode == 200) {
                if (data.getData() != null) {
                    Uri uri = data.getData();

                    binding.userProfile.setImageURI(uri);
                    progressDialog.show();
                    final StorageReference reference = storage.getReference().child("profilePhoto").child(auth.getUid());

                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    database.getReference().child("Users").child(auth.getUid()).child("profilePhoto").setValue(uri.toString());
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });


                }
            }
        }
    }


}