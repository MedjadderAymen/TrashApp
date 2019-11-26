package com.medjay.trashapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.medjay.trashapp.R;
import com.medjay.trashapp.ViewPagerAdapter;


public class Home extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout=view.findViewById(R.id.tabs);
        viewPager=view.findViewById(R.id.container);

        ViewPagerAdapter adapter=new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new List(),"Se connecter");
        adapter.addFragment(new Maps(),"S'inscrire");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
