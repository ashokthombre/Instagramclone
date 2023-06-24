package com.example.instagramclone.Frgments;

import static android.app.Activity.RESULT_CANCELED;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagramclone.Models.PostModel;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.FragmentAddBinding;
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

import java.util.Date;


public class AddFragment extends Fragment {
FragmentAddBinding binding;

FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
Uri uri;

ProgressDialog dialog;


    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   dialog=new ProgressDialog(getContext());
   dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   dialog.setTitle("Post Uploading");
   dialog.setMessage("Please wait.....");
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        database.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user=snapshot.getValue(User.class);
                        binding.name.setText(user.getName());
                        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile)
                                .into(binding.profilePic);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddBinding.inflate(inflater,container, false);

        binding.etPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
           String description=binding.etPost.getText().toString();
           if (!description.isEmpty())
           {

               binding.btnpost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_btn));
               binding.btnpost.setTextColor(getContext().getResources().getColor(R.color.white));
               binding.btnpost.setEnabled(true);

           }
           else
           {
               binding.btnpost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_activebtn));
               binding.btnpost.setTextColor(getContext().getResources().getColor(R.color.black));
               binding.btnpost.setEnabled(false);
           }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,300);


            }
        });

        binding.btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uri==null)
                {

                    Toast.makeText(getContext(), "Please Attach image to Post..", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();



         final StorageReference reference=storage.getReference().child("posts")
                 .child(auth.getUid())
                 .child(new Date().getTime()+"");

                 reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                         reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {

                                 PostModel postModel=new PostModel();
                                 postModel.setPostImage(uri.toString());
                                 postModel.setPostedBy(auth.getUid());
                                 postModel.setPostDescription(binding.etPost.getText().toString());
                                 postModel.setPostAt(new Date().getTime());

                                 database.getReference().child("posts")
                                         .push().setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void unused) {

                                                 Toast.makeText(getContext(), "Post Added Successfully !", Toast.LENGTH_SHORT).show();
                                                 dialog.dismiss();

                                                 HomeFragment homeFragment=new HomeFragment();
                                                 FragmentTransaction tr=getActivity().getSupportFragmentManager().beginTransaction();
                                                 tr.replace(R.id.container,homeFragment);
                                                 tr.commit();

                                             }
                                         });
                             }
                         });
                     }
                 });

            }
        });





        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode!=RESULT_CANCELED)
        {
            if (requestCode==300)
            {
                if (data.getData()!=null)
                {
                    uri=data.getData();

                    binding.postImage.setImageURI(uri);
                    binding.postImage.setVisibility(View.VISIBLE);


                }
            }
        }

    }
}