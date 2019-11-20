package com.medjay.trashapp;


import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    private final List<Fragment> fragmentlist=new ArrayList<>();
    private final List<String> FragmentListeTitel=new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentListeTitel.get(position);
    }
    public void addFragment(Fragment fragment,String Title){
        fragmentlist.add(fragment);
        FragmentListeTitel.add(Title);
    }
}
