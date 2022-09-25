package com.example.movieapiretrofit.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.movieapiretrofit.R;
import com.example.movieapiretrofit.adapter.TabAdapter;
import com.example.movieapiretrofit.databinding.ActivityHomeBinding;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //implementasi view binding
        setContentView(binding.getRoot());
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setupTab();
    }

    private void setupTab(){
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new PopularFragment(), "Popular");
        adapter.addFragment(new NowPlayingFragment(), "Now Playing");

        //implementasi view binding
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }
}