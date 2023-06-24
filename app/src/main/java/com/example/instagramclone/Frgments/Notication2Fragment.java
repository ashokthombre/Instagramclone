package com.example.instagramclone.Frgments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagramclone.Adapters.NotificationAdapter;
import com.example.instagramclone.Models.NotificationModel;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Notication2Fragment extends Fragment {

   RecyclerView recyclerView;
   ArrayList<NotificationModel>models;

   FirebaseDatabase database;
   FirebaseAuth auth;


    public Notication2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notication2, container, false);

        recyclerView=view.findViewById(R.id.notificationRv);
        models=new ArrayList<>();

        NotificationAdapter adapter=new NotificationAdapter(getContext(),models);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        database.getReference().child("notification")
                .child(auth.getUid())
             .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        models.clear();
                       for (DataSnapshot dataSnapshot:snapshot.getChildren())
                       {
                           NotificationModel notificationModel=dataSnapshot.getValue(NotificationModel.class);
                           notificationModel.setNotificationId(dataSnapshot.getKey());
                           models.add(notificationModel);

                       }
                       adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


      return view;
    }
}