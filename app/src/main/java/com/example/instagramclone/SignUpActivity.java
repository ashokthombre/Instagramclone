package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.instagramclone.Models.User;
import com.example.instagramclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

 FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Registering..");
        progressDialog.setMessage("Please wait");

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.btnlsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.userEmail.getText().toString().isEmpty())
                {
                    binding.userEmail.setError("Field is empty");
                    return;
                }
                if (binding.userPassword.getText().toString().isEmpty())
                {
                    binding.userPassword.setError("Field is empty");
                    return;
                }
                if (binding.username.getText().toString().isEmpty())
                {
                    binding.username.setError("Field is empty");
                    return;
                }
                if (binding.profession.getText().toString().isEmpty())
                {
                    binding.profession.setError("Field is empty");
                    return;
                }

                String email=binding.userEmail.getText().toString();
                String password=binding.userPassword.getText().toString();
                String name=binding.username.getText().toString();
                String prfession=binding.profession.getText().toString();
                progressDialog.show();

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            progressDialog.show();

                            User user=new User(name,prfession,email,password);

                            database.getReference().child("Users").child(auth.getUid()).setValue(user);

                            Toast.makeText(SignUpActivity.this, "Suuccessfully Register", Toast.LENGTH_SHORT).show();
                           progressDialog.dismiss();
                            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else
                        {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                         progressDialog.dismiss();
                        }
                    }
                });



            }
        });

        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}