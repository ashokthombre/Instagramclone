package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.instagramclone.Frgments.AddFragment;
import com.example.instagramclone.Frgments.HomeFragment;
import com.example.instagramclone.Frgments.NotificationFragment;
import com.example.instagramclone.Frgments.ProfileFragment;
import com.example.instagramclone.Frgments.SearchFragment;

import com.example.instagramclone.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
 ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HomeFragment homeFragment=new HomeFragment();
        FragmentTransaction tr=getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.container,homeFragment);
        tr.commit();


        binding.bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.home)
                {
                    HomeFragment homeFragment=new HomeFragment();
                    FragmentTransaction tr=getSupportFragmentManager().beginTransaction();
                    tr.replace(R.id.container,homeFragment);
                    tr.commit();
                    
                }

                else if (item.getItemId()==R.id.search)
                {
                    FragmentTransaction tr=getSupportFragmentManager().beginTransaction();
                    tr.replace(R.id.container,new SearchFragment());
                    tr.commit();
                }
                else if (item.getItemId()==R.id.add)
                {
                    FragmentTransaction tr=getSupportFragmentManager().beginTransaction();
                    tr.replace(R.id.container,new AddFragment());
                    tr.commit();
                } else if (item.getItemId()==R.id.notification) {
                    FragmentTransaction tr=getSupportFragmentManager().beginTransaction();
                    tr.replace(R.id.container,new NotificationFragment());
                    tr.commit();
                }
                else
                {
                    FragmentTransaction tr=getSupportFragmentManager().beginTransaction();
                    tr.replace(R.id.container,new ProfileFragment());
                    tr.commit();
                }

                return true;
            }
        });


    }
}