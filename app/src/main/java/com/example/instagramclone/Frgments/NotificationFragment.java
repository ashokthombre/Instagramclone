package com.example.instagramclone.Frgments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Adapters.ViewPagerAdapter;
import com.example.instagramclone.R;
import com.google.android.material.tabs.TabLayout;


public class NotificationFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tablyout;
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);

        viewPager=view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager()));
        tablyout=view.findViewById(R.id.tablayout);

        tablyout.setupWithViewPager(viewPager);

        Notication2Fragment notication2Fragment=new Notication2Fragment();
        FragmentManager fm=((AppCompatActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();
        tr.replace(R.id.container,notication2Fragment);
        tr.commit();

    return view;
    }
}